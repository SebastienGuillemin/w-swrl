package com.sebastienguillemin.wswrl.rule.variable.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.binding.BindingCache;
import com.sebastienguillemin.wswrl.core.rule.variable.binding.VariableBinding;

/**
 * {@inheritDoc}
 */
public class DefaultVariableBinding implements VariableBinding {
    private Set<WSWRLAtom> atoms;
    private Hashtable<IRI, WSWRLIndividual> individuals;
    private Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals;

    private Set<WSWRLClassAtom> classAtoms;
    private Set<WSWRLObjectPropertyAtom> objectPropertyAtoms;
    private Set<WSWRLDataPropertyAtom> dataPropertyAtoms;

    private Set<WSWRLVariable> classAtomVariables;
    private Set<WSWRLVariable> subjectVariables;
    private Set<WSWRLVariable> objectVariables;
    private Set<WSWRLVariable> allVariables;

    private Set<WSWRLIVariable> iVariables;
    private Set<WSWRLDVariable> dVariables;

    private BindingCache<WSWRLIVariable, WSWRLIndividual> bindingCacheSubject;
    private BindingCache<WSWRLIVariable, WSWRLIndividual> bindingCacheObject;
    private BindingCache<WSWRLDVariable, OWLLiteral> bindingCacheData;

    public DefaultVariableBinding(Set<WSWRLAtom> atoms, Hashtable<IRI, WSWRLIndividual> individuals,
            Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals) {
        this.atoms = atoms;
        this.individuals = individuals;
        this.classToIndividuals = classToIndividuals;

        this.sortAtoms();
        this.sortVariables();

        // System.out.println("--> [DefaultVariableBinding] classAtoms: " + classAtoms);
        // System.out.println("--> [DefaultVariableBinding] objectPropertyAtoms: " +
        // objectPropertyAtoms);
        // System.out.println("--> [DefaultVariableBinding] dataPropertyAtoms: " +
        // dataPropertyAtoms);

        // System.out.println("\n--> [DefaultVariableBinding] classAtomVariables: " +
        // classAtomVariables);
        // System.out.println("--> [DefaultVariableBinding] subjectVariables: " +
        // subjectVariables);
        // System.out.println("--> [DefaultVariableBinding] objectVariables: " +
        // objectVariables);

        this.initCaches();
    }

    @Override
    public void nextBinding() {
        if (this.bindingCacheData.isLocked()) {
            this.bindingCacheData.next();
            this.bindingCacheData.bind();
        } else if (this.bindingCacheObject.isLocked()) {
            this.bindingCacheObject.next();
            this.bindingCacheObject.bind();
            this.bindData();
        } else if (this.bindingCacheSubject.isLocked()) {
            this.clearVariableBindings();
            this.bindingCacheSubject.next();
            this.bindingCacheSubject.bind();
            this.bindObject();
            this.bindData();
        }
        this.printBinding();
    }

    // TODO : à supprimer
    private void printBinding() {
        String bindings = "";
        for (WSWRLIVariable iVariable : this.iVariables) {
            bindings += iVariable.getIRI().getFragment() + " -> "
                    + ((iVariable.getValue() == null) ? "null" : iVariable.getValue().getIRI().getFragment()) + ", ";
        }

        for (WSWRLDVariable dVariable : this.dVariables) {
            bindings += dVariable.getIRI().getFragment() + " -> "
                    + ((dVariable.getValue() == null) ? "null" : dVariable.getValue().getLiteral()) + ", ";
        }

        System.out.println(bindings);
    }

    @Override
    public boolean hasNext() {
        return this.bindingCacheSubject.isLocked()
                || this.bindingCacheData.isLocked()
                || this.bindingCacheObject.isLocked();
    }

    private void sortAtoms() {
        this.classAtoms = this.atoms.stream().filter(a -> a instanceof WSWRLClassAtom).map(a -> (WSWRLClassAtom) a)
                .collect(Collectors.toSet());
        this.objectPropertyAtoms = this.atoms.stream().filter(a -> a instanceof WSWRLObjectPropertyAtom)
                .map(a -> (WSWRLObjectPropertyAtom) a).collect(Collectors.toSet());
        this.dataPropertyAtoms = this.atoms.stream().filter(a -> a instanceof WSWRLDataPropertyAtom)
                .map(a -> (WSWRLDataPropertyAtom) a).collect(Collectors.toSet());
    }

    private void sortVariables() {
        this.classAtomVariables = this.classAtoms.stream().map(a -> a.getWSWRLArgument()).collect(Collectors.toSet());

        this.subjectVariables = new HashSet<>(this.classAtomVariables);
        this.subjectVariables
                .addAll(this.objectPropertyAtoms.stream().map(a -> a.getSubject()).collect(Collectors.toSet()));
        this.subjectVariables
                .addAll(this.dataPropertyAtoms.stream().map(a -> a.getSubject()).collect(Collectors.toSet()));

        this.objectVariables = this.objectPropertyAtoms.stream().map(a -> a.getObject()).collect(Collectors.toSet());
        this.objectVariables
                .addAll(this.dataPropertyAtoms.stream().map(a -> a.getObject()).collect(Collectors.toSet()));

        this.allVariables = new HashSet<>();
        this.allVariables.addAll(this.subjectVariables);
        this.allVariables.addAll(this.objectVariables);

        this.iVariables = this.allVariables.stream().filter(v -> v instanceof WSWRLIVariable)
                .map(v -> (WSWRLIVariable) v).collect(Collectors.toSet());
        this.dVariables = this.allVariables.stream().filter(v -> v instanceof WSWRLDVariable)
                .map(v -> (WSWRLDVariable) v).collect(Collectors.toSet());
    }

    private void initCaches() {
        HashMap<WSWRLIVariable, List<WSWRLIndividual>> values = new HashMap<>();

        // Process class atoms
        Set<WSWRLIndividual> individualsByClass;
        for (WSWRLClassAtom atom : this.classAtoms) {
            individualsByClass = this.classToIndividuals.get(atom.getIRI());
            values.put(atom.getWSWRLArgument(),
                    new ArrayList<>(Optional.ofNullable(individualsByClass).orElse(new HashSet<>())));
        }

        // Process variables which are subjects of an atom but
        // not object of another atom and not in a class atom
        Set<WSWRLVariable> subjectNotObject = new HashSet<>();
        subjectNotObject.addAll(this.subjectVariables);
        subjectNotObject.removeAll(this.objectVariables);
        subjectNotObject.removeAll(this.classAtomVariables);

        List<WSWRLIndividual> allIndividualsList = new ArrayList<>(this.individuals.values());
        WSWRLIVariable variable;
        for (WSWRLObjectPropertyAtom atom : this.objectPropertyAtoms) {
            variable = atom.getSubject();
            if (subjectNotObject.contains(variable)) {
                // TODO : optimiser cette ligne en filtrant les individus.
                values.put(variable, allIndividualsList);
                subjectNotObject.remove(variable);
            }
        }

        if (subjectNotObject.size() != 0)
            for (WSWRLDataPropertyAtom atom : this.dataPropertyAtoms) {
                variable = atom.getSubject();
                if (subjectNotObject.contains(variable)) {
                    // TODO : optimiser cette ligne en filtrant les individus.
                    values.put(variable, allIndividualsList);
                    subjectNotObject.remove(variable);
                }
            }

        // Create caches
        this.bindingCacheSubject = new DefaultBindingCache<>(values);
        // Lock the cache of subject atoms by default
        this.bindingCacheSubject.lock();

        this.bindingCacheObject = new DefaultBindingCache<>();
        this.bindingCacheData = new DefaultBindingCache<>();
    }

    private void clearVariableBindings() {
        for (WSWRLVariable variable : this.allVariables)
            if (variable instanceof WSWRLIVariable)
                ((WSWRLIVariable) variable).setValue(null);
            else
                ((WSWRLDVariable) variable).setValue(null);
    }

    private void bindObject() {
        // Clear object cache.
        this.bindingCacheObject.clear();

        // Retrieve object properties atoms whose the subject or the object is not set.
        Set<WSWRLObjectPropertyAtom> objectPropertyAtomsToProcess = this.objectPropertyAtoms.stream()
                .filter(ob -> ob.getSubject().getValue() == null || ob.getObject().getValue() == null)
                .collect(Collectors.toSet());
        boolean update = objectPropertyAtomsToProcess.size() > 0;

        // Process atoms.
        do {
            objectPropertyAtomsToProcess = this.bindVariablesInObjectProperties(objectPropertyAtomsToProcess);
            this.bindingCacheObject.bind();
        } while (objectPropertyAtomsToProcess.size() > 0);

        // If at least one atom was processed -> lock.
        if (update) {
            this.bindingCacheObject.lock();

            // Call 'next' because 'bind' was called previously.
            this.bindingCacheObject.next();
        }
    }

    private void bindData() {
        // Clear data cache.
        this.bindingCacheData.clear();

        // Retrieve data properties atoms whose the object is not set.
        Set<WSWRLDataPropertyAtom> dataPropertyAtomsToProcess = this.dataPropertyAtoms.stream()
                .filter(dt -> dt.getObject().getValue() == null)
                .collect(Collectors.toSet());
        boolean update = dataPropertyAtomsToProcess.size() > 0;

        // Process atoms.
        do {
            dataPropertyAtomsToProcess = this.bindVariablesInDataProperties(dataPropertyAtomsToProcess);
            this.bindingCacheData.bind();
        } while (dataPropertyAtomsToProcess.size() > 0);

        // If at least one atom was processed -> lock.
        if (update) {
            this.bindingCacheData.lock();

            // Call 'next' because 'bind' was called previously.
            this.bindingCacheData.next();
        }
    }

    private Set<WSWRLObjectPropertyAtom> bindVariablesInObjectProperties(
            Set<WSWRLObjectPropertyAtom> objectPropertyAtoms) {
        Set<WSWRLObjectPropertyAtom> remainingAtoms = new HashSet<>();

        WSWRLIVariable subject, object;
        WSWRLIndividual subjectIndividual;
        List<WSWRLIndividual> values;
        for (WSWRLObjectPropertyAtom atom : objectPropertyAtoms) {
            values = new ArrayList<>();
            subject = atom.getSubject();
            object = atom.getObject();
            subjectIndividual = subject.getValue();
            if (subjectIndividual != null) {
                values = subjectIndividual.getObjectProperties(atom.getIRI()).stream()
                        .map(axiom -> this.individuals.get(axiom.getObject().asOWLNamedIndividual().getIRI()))
                        .collect(Collectors.toList());
                this.bindingCacheObject.addValues(object, values);
            } else if (subject.isUnboundable())
                this.bindingCacheObject.addValues(object, new ArrayList<>());
            else
                remainingAtoms.add(atom);
        }

        return remainingAtoms;
    }

    private Set<WSWRLDataPropertyAtom> bindVariablesInDataProperties(Set<WSWRLDataPropertyAtom> dataPropertyAtoms) {
        Set<WSWRLDataPropertyAtom> remainingAtoms = new HashSet<>();

        WSWRLIVariable subject;
        WSWRLDVariable object;
        WSWRLIndividual subjectIndividual;
        List<OWLLiteral> values;
        for (WSWRLDataPropertyAtom atom : dataPropertyAtoms) {
            values = new ArrayList<>();
            subject = atom.getSubject();
            object = atom.getObject();
            subjectIndividual = subject.getValue();
            if (subjectIndividual != null) {
                values = subjectIndividual.getDataProperties(atom.getIRI()).stream().map(a -> a.getObject())
                        .collect(Collectors.toList());

                // System.out.println("Data values for '" + atom.getIRI().getFragment() + "' and
                // subject:" + subject.getValue().getIRI().getFragment() + ": " +
                // (values.stream().map(l -> l.getLiteral()).collect(Collectors.toList())));

                this.bindingCacheData.addValues(object, values);
            } else if (subject.isUnboundable())
                this.bindingCacheData.addValues(object, new ArrayList<>());
            else
                remainingAtoms.add(atom);
        }

        return remainingAtoms;
    }
}
