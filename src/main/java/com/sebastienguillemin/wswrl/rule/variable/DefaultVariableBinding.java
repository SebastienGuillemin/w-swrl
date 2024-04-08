package com.sebastienguillemin.wswrl.rule.variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;

import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.VariableBinding;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;

/**
 * {@inheritDoc}
 */
public class DefaultVariableBinding implements VariableBinding {
    public static long nextIndividualsBinding = 0, bindIndividuals = 0, processDataProperties = 0, nextDataBinding = 0,
            bindDataVariables = 0;

    private Set<WSWRLAtom> atoms;

    // TODO : utiliser WSWRLIVariable ?
    private HashMap<WSWRLVariable, List<WSWRLIndividual>> individualBindings; // Individual variable -> individuals.
    private HashMap<IRI, Integer> individialPointers; // (Individual) Variables IRI -> Integer.

    private HashMap<WSWRLDVariable, List<OWLLiteral>> dataBindings; // Data variable -> litteral.
    private HashMap<IRI, Integer> dataPointers; // (Data) Variables IRI -> Integer

    public boolean dataLock;
    private int bindingPossibilities;
    private int processedBindings;
    private Set<WSWRLDataPropertyAtom> dataPropertyAtoms;
    private Set<WSWRLClassAtom> classAtoms;

    public DefaultVariableBinding(Set<WSWRLAtom> atoms, Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals) {
        this.atoms = atoms;
        this.individualBindings = new HashMap<>();
        this.dataBindings = new HashMap<>();
        this.dataPointers = new HashMap<>();
        this.dataLock = false;
        this.individialPointers = new HashMap<>();
        this.bindingPossibilities = 1;
        this.dataPropertyAtoms = this.atoms.stream()
                .filter(a -> a instanceof WSWRLDataPropertyAtom).map(a -> (WSWRLDataPropertyAtom) a)
                .collect(Collectors.toSet());

        this.init(classToIndividuals);
    }

    private void init(Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals) {
        // Init individuals bindings
        this.processIndividualBindings(classToIndividuals);

        // Init individuals pointers.
        this.initIndividiualsPointers();
    }

    private void processIndividualBindings(Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals) {
        // Finding all class atom
        this.classAtoms = this.atoms.stream().filter(atom -> (atom instanceof WSWRLClassAtom))
                .map(atom -> (WSWRLClassAtom) atom).collect(Collectors.toSet());

        // Filtering individuals.
        // int individualBindingPossiblities = 1;

        IRI classAtomIRI;
        for (WSWRLClassAtom classAtom : classAtoms) {
            classAtomIRI = classAtom.getIRI();
            for (WSWRLVariable variable : classAtom.getVariables()) {

                if (classToIndividuals.containsKey(classAtomIRI))
                    this.individualBindings.put(variable, new ArrayList<>(classToIndividuals.get(classAtomIRI)));
                else
                    this.individualBindings.put(variable, new ArrayList<>());

                // individualBindingPossiblities *=
                // this.individualBindings.get(variable).size();
            }
        }

        // System.out.println("\nIndividuals binding possibilities: " +
        // individualBindingPossiblities);
    }

    private void initIndividiualsPointers() {
        WSWRLVariable variable;
        List<WSWRLIndividual> individuals;
        for (Entry<WSWRLVariable, List<WSWRLIndividual>> entry : this.individualBindings.entrySet()) {
            variable = entry.getKey();
            individuals = entry.getValue();
            this.individialPointers.put(variable.getIRI(), 0);

            this.bindingPossibilities *= individuals.size();
        }
    }

    @Override
    public boolean hasNext() {
        return this.processedBindings < this.bindingPossibilities;
    }

    @Override
    public void nextBinding() {
        long start = System.currentTimeMillis();
        long check1, check2, check3, check4, check5;
        if (dataLock) {
            this.nextDataBinding();
            check1 = System.currentTimeMillis();

            this.bindDataVariables();
            check2 = System.currentTimeMillis();

            nextDataBinding += (check1 - start);
            bindDataVariables += (check2 - check1);
        } else {
            this.nextIndividualsBinding();
            check1 = System.currentTimeMillis();

            this.bindIndividuals();
            check2 = System.currentTimeMillis();

            this.processDataProperties();
            check3 = System.currentTimeMillis();

            this.nextDataBinding();
            check4 = System.currentTimeMillis();

            this.bindDataVariables();
            check5 = System.currentTimeMillis();

            nextIndividualsBinding += (check1 - start);
            bindIndividuals += (check2 - check1);
            processDataProperties += (check3 - check2);
            nextDataBinding += (check4 - check3);
            bindDataVariables += (check5 - check4);
        }

        // Print bindings
        // String bindings = "";
        // for (Entry<WSWRLVariable, List<WSWRLIndividual>> entry :
        // this.individualBindings.entrySet())
        // bindings += entry.getKey().getIRI().getFragment() + " <- "
        // +
        // entry.getValue().get(this.individialPointers.get(entry.getKey().getIRI())).getIRI().getFragment()
        // + ", ";

        // for (Entry<WSWRLDVariable, List<OWLLiteral>> entry :
        // this.dataBindings.entrySet()) {
        // WSWRLDVariable variable = entry.getKey();
        // List<OWLLiteral> values = entry.getValue();
        // if (values == null || values.size() == 0)
        // bindings += variable.getIRI().getFragment() + " <- " + "'', ";
        // else
        // bindings += variable.getIRI().getFragment() + " <- "
        // + values.get(this.dataPointers.get(variable.getIRI())).getLiteral() + ", ";
        // }

        // System.out.println(bindings);
    }

    private void bindIndividuals() {
        WSWRLVariable iVariable;
        List<WSWRLIndividual> iValues;
        for (Entry<WSWRLVariable, List<WSWRLIndividual>> entry : this.individualBindings.entrySet()) {
            iVariable = entry.getKey();
            iValues = entry.getValue();
            ((WSWRLIVariable) iVariable).setValue(iValues.get(this.individialPointers.get(iVariable.getIRI())));
        }
    }

    private void bindDataVariables() {
        WSWRLDVariable dVariable;
        List<OWLLiteral> oValues;
        for (Entry<WSWRLDVariable, List<OWLLiteral>> entry : this.dataBindings.entrySet()) {
            dVariable = entry.getKey();
            oValues = entry.getValue();

            if (oValues == null || oValues.size() == 0)
                continue;
            dVariable.setValue(oValues.get(this.dataPointers.get(dVariable.getIRI())));
        }
    }

    private void nextIndividualsBinding() {
        // If some data are not processed -> lock
        if (this.dataLock)
            return;

        // Increase processed binding counter
        this.processedBindings++;

        // Next individual.
        int pointer = 0;
        IRI variableIRI;
        for (WSWRLVariable variable : this.individualBindings.keySet()) {
            variableIRI = variable.getIRI();
            pointer = this.individialPointers.get(variableIRI);
            pointer = ++pointer % this.individualBindings.get(variable).size();
            this.individialPointers.put(variableIRI, pointer);

            if (pointer != 0)
                break;
        }
    }

    private void nextDataBinding() {
        int pointer = 0;
        IRI variableIRI;
        int valuesCount;
        for (WSWRLDVariable variable : this.dataBindings.keySet()) {
            valuesCount = this.dataBindings.get(variable).size();
            if (valuesCount == 0)
                continue;

            variableIRI = variable.getIRI();
            pointer = this.dataPointers.get(variableIRI);
            pointer = ++pointer % this.dataBindings.get(variable).size();
            this.dataPointers.put(variableIRI, pointer);

            if (pointer != 0)
                return;
        }
        this.dataLock = false;
    }

    private void processDataProperties() {
        this.dataBindings.clear();
        this.dataPointers.clear();
        // Get data properties

        // For each data property atoms
        List<OWLLiteral> literals;
        WSWRLIndividual subject;
        for (WSWRLDataPropertyAtom atom : this.dataPropertyAtoms) {
            literals = new ArrayList<>();
            subject = atom.getSubject().getValue();

            // Get the subject data property assertions and add the object to the litterals
            // list for the WSWRLDVariable.
            for (OWLDataPropertyAssertionAxiom assertion : subject.getDataProperties(atom.getIRI()))
                literals.add(assertion.getObject());

            this.dataBindings.put(atom.getObject(), literals);
            this.dataPointers.put(atom.getObject().getIRI(), 0);
        }

        // Lock
        this.dataLock = true;
    }
}
