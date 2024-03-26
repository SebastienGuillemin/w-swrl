package com.sebastienguillemin.wswrl.engine.target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.VariableBinding;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.rule.DefaultWSWRLIndividual;
import com.sebastienguillemin.wswrl.rule.variable.DefaultVariableBinding;

/**
 * {@inheritDoc}
 */
public class DefaultTargetWSWRLRuleEngine implements TargetWSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;
    private OWLOntologyManager ontologyManager;
    private Hashtable<IRI, WSWRLIndividual> individuals;

    /**
     * Constructor.
     * @param WSWRLOntology The ontology to used to create the target rule engine.
     */
    public DefaultTargetWSWRLRuleEngine(WSWRLOntology WSWRLOntology, OWLOntologyManager ontologyManager) {
        this.wswrlOntology = WSWRLOntology;
        this.ontologyManager = ontologyManager;
        this.individuals = new Hashtable<>();
    }

    @Override
    public void runRuleEngine() {
        try {
            this.reset();
            this.processOntology();

            // Processing WSWRL rules.
            Set<WSWRLRule> wswrlRules = wswrlOntology.getWSWRLRules();
            for (WSWRLRule rule : wswrlRules) {
                if (!rule.isEnabled())
                    continue;

                Set<WSWRLAtom> body = rule.getBody();
                for (VariableBinding binding : this.generateBindings(body)) {
                    binding.bindVariables();
                    
                    // Calculate rank weights
                    rule.calculateWeights();
                    
                    // Evaluate
                    float confidence = rule.calculateConfidence();
                    
                    // Store result
                    if (confidence > 0)
                        wswrlOntology.addInferredAxiom(rule.getHead(), confidence);
                }
            }
            ontologyManager.addAxioms(this.wswrlOntology.getOWLOntology(), this.wswrlOntology.getInferredAxioms());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        this.individuals = new Hashtable<>();
    }

    private void processOntology() throws Exception {
        for (OWLAxiom axiom : this.wswrlOntology.getOWLAxioms()) {
            if (axiom.isOfType(AxiomType.DECLARATION)) {
                OWLEntity entity = ((OWLDeclarationAxiom) axiom).getEntity();

                if (entity.isOWLNamedIndividual())
                    this.addIndividual(new DefaultWSWRLIndividual((OWLNamedIndividual) entity));
            } else if (axiom.isOfType(AxiomType.DATA_PROPERTY_ASSERTION)) {
                OWLDataPropertyAssertionAxiom assertionAxiom = (OWLDataPropertyAssertionAxiom) axiom;
                OWLNamedIndividual owlIndividual = (OWLNamedIndividual) assertionAxiom.getSubject();

                WSWRLIndividual wswrlIndividual = this.getOrCreateIndividual(owlIndividual);
                wswrlIndividual.addDataProperty(assertionAxiom);
            } else if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
                OWLObjectPropertyAssertionAxiom assertionAxiom = (OWLObjectPropertyAssertionAxiom) axiom;
                OWLNamedIndividual owlIndividual = (OWLNamedIndividual) assertionAxiom.getSubject();

                WSWRLIndividual wswrlIndividual = this.getOrCreateIndividual(owlIndividual);
                wswrlIndividual.addObjectProperty(assertionAxiom);
            } else if (axiom.isOfType(AxiomType.CLASS_ASSERTION)) {
                OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) axiom;
                OWLNamedIndividual owlIndividual = (OWLNamedIndividual) classAssertionAxiom.getIndividual();

                WSWRLIndividual wswrlIndividual = this.getOrCreateIndividual(owlIndividual);
                wswrlIndividual.addOWLClass(classAssertionAxiom.getClassExpression().asOWLClass());
            }
        }
    }

    private WSWRLIndividual getOrCreateIndividual(OWLNamedIndividual owlIndividual) {
        WSWRLIndividual wswrlIndividual = this.individuals.get(owlIndividual.getIRI());

        if (wswrlIndividual == null) {
            wswrlIndividual = new DefaultWSWRLIndividual(owlIndividual);
            this.addIndividual(wswrlIndividual);
        }

        return wswrlIndividual;
    }

    private void addIndividual(WSWRLIndividual individual) {
        IRI iri = individual.getIRI();
        if (!this.individuals.containsKey(iri))
            this.individuals.put(iri, individual);
    }

    private List<VariableBinding> generateBindings(Set<WSWRLAtom> atoms) {
        Set<WSWRLVariable> variables = this.getAllVariables(atoms);

        // Bind individual variables.
        List<VariableBinding> individualBindings = this.generateIndividualBindings(variables);

        // Bind data variables.
        List<VariableBinding> completeBindings = this.generateDataBindings(individualBindings, atoms, variables);

        return completeBindings;
    }

    private Set<WSWRLVariable> getAllVariables(Set<WSWRLAtom> atoms) {
        Set<WSWRLVariable> variables = new HashSet<>();

        for (WSWRLAtom atom : atoms)
            for (WSWRLVariable variable : atom.getVariables())
                variables.add(variable);
        return variables;
    }

    private List<VariableBinding> generateIndividualBindings(Set<WSWRLVariable> variables) {
        List<VariableBinding> bindings = new ArrayList<>();

        List<WSWRLIVariable> individualVariables = variables.stream()
                .filter(v -> v.getDomain() == WSWRLVariableDomain.INDIVIDUALS).map(v -> (WSWRLIVariable) v)
                .collect(Collectors.toList());
        List<WSWRLIndividual> boundableIndividual = new ArrayList<>(this.individuals.values());
        int individualBindingPossiblities = (int) Math.pow(boundableIndividual.size(), individualVariables.size());

        System.out.println("\nBoundable individuals count : " + boundableIndividual.size() + " => "
                + individualBindingPossiblities + " individual binding possibilities.\n");

        int individualPointer = 0;
        int change, count;
        for (int v = 0; v < individualVariables.size(); v++) {
            change = (int) Math.pow(Math.max(boundableIndividual.size(), individualVariables.size()),
                    individualVariables.size() - (v + 1));

            count = 0;
            for (int i = 0; i < individualBindingPossiblities; i++) {
                VariableBinding binding;
                if (v == 0) {
                    binding = new DefaultVariableBinding();
                    bindings.add(binding);
                } else
                    binding = bindings.get(i);

                WSWRLIVariable variableName = individualVariables.get(v);
                binding.bindIndividual(variableName,
                        boundableIndividual.get(individualPointer));

                count = (++count) % change;
                if (count == 0)
                    individualPointer = (++individualPointer) % boundableIndividual.size();
            }
        }

        return bindings;
    }

    private List<VariableBinding> generateDataBindings(List<VariableBinding> individualBindings, Set<WSWRLAtom> atoms,
            Set<WSWRLVariable> variables) {

        List<WSWRLDataPropertyAtom> dataPropertyAtoms = atoms.stream().filter(a -> a instanceof WSWRLDataPropertyAtom)
                .map(a -> (WSWRLDataPropertyAtom) a)
                .collect(Collectors.toList());

        List<VariableBinding> newBindings = new ArrayList<>();

        if (dataPropertyAtoms.size() == 0)
            return individualBindings;
        else {
            System.out.println(dataPropertyAtoms.size() + " data property atoms found\n");

            WSWRLIndividual individual;
            VariableBinding newBinding;
            boolean boundValue;
            for (WSWRLDataPropertyAtom atom : dataPropertyAtoms) {
                WSWRLIVariable atomSubject = atom.getSubject();
                WSWRLDVariable atomObject = atom.getObject();

                for (VariableBinding binding : individualBindings) {
                    individual = binding.getIndividualValue(atomSubject.getIRI());

                    boundValue = false;
                    for (OWLDataPropertyAssertionAxiom axiom : individual.getDataProperties(atom.getIRI())) {
                        // Copy of binding
                        newBinding = new DefaultVariableBinding((DefaultVariableBinding) binding);
                        newBinding.bindLiteral(atomObject, axiom.getObject());
                        newBindings.add(newBinding);
                        boundValue = true;
                    }

                    if (!boundValue) {
                        newBinding = new DefaultVariableBinding((DefaultVariableBinding) binding);
                        newBinding.bindLiteral(atomObject, null);
                        newBindings.add(newBinding);
                    }
                }
            }
            return newBindings;
        }

    }
}
