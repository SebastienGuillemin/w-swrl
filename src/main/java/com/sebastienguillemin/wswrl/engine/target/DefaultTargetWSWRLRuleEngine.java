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

import com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.VariableBinding;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.rule.variable.DefaultVariableBinding;
import com.sebastienguillemin.wswrl.rule.variable.DefaultWSWRLIndividual;

public class DefaultTargetWSWRLRuleEngine implements TargetWSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;

    private Hashtable<IRI, WSWRLIndividual> individuals;

    public DefaultTargetWSWRLRuleEngine(WSWRLOntology WSWRLOntology) {
        this.wswrlOntology = WSWRLOntology;

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
                Set<WSWRLVariable> variables = this.getAllVariables(body);

                for (VariableBinding binding : this.generateBindings(variables)) {
                    binding.bindVariables(this.individuals);
                    
                    // Calculate rank weights
                    rule.calculateWeights();
                    
                    // Evaluate
                    float confidence = rule.calculateConfidence();
                    if (confidence > 0) {
                        System.out.println(binding);
                        System.out.println("Confidence : " + confidence + "\n");
                    }

                    // TODO: Store result

                }
            }
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
        if (!this.individuals.containsKey(iri)) {
            this.individuals.put(iri, individual);
        }
    }

    private Set<WSWRLVariable> getAllVariables(Set<WSWRLAtom> atoms) {
        Set<WSWRLVariable> variables = new HashSet<>();

        for (WSWRLAtom atom : atoms) {
            for (WSWRLVariable variable : atom.getVariables()) {
                variables.add(variable);
            }
        }

        return variables;
    }

    private List<VariableBinding> generateBindings(Set<WSWRLVariable> variables) {
        List<VariableBinding> bindings = this.generateIndividualBindings(variables);

        return bindings;
    }

    private List<VariableBinding> generateIndividualBindings(Set<WSWRLVariable> variables) {
        List<VariableBinding> bindings = new ArrayList<>();

        List<WSWRLVariable> individualVariables = variables.stream()
                .filter(v -> v.getDomain() == WSWRLVariableDomain.INDIVIDUALS).collect(Collectors.toList());
        List<WSWRLIndividual> boundableIndividual = new ArrayList<>(this.individuals.values());
        int individualBindingPossiblities = (int) Math.pow(boundableIndividual.size(), individualVariables.size());

        System.out.println("\nBoundable individual count : " + boundableIndividual.size());
        System.out.println(individualBindingPossiblities + " individual binding possibilities.\n");

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

                WSWRLVariable variableName = individualVariables.get(v);
                binding.bindIndividual(variableName,
                        boundableIndividual.get(individualPointer).getIRI());

                count = (++count) % change;
                if (count == 0)
                    individualPointer = (++individualPointer) % boundableIndividual.size();
            }
        }

        return bindings;
    }

    // private void bind(VariableBinding binding) {
    // throw new UnsupportedOperationException("Unimplemented method 'bind'");
    // }
}
