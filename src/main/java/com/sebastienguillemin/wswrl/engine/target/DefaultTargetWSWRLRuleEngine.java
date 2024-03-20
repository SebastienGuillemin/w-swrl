package com.sebastienguillemin.wswrl.engine.target;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.WSWRLVariable;
import com.sebastienguillemin.wswrl.engine.target.wrapper.WSWRLIndividualWrapper;

public class DefaultTargetWSWRLRuleEngine implements TargetWSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;
    private OWLReasonerFactory owlReasonerFactory;
    // private OWLReasoner owlReasoner;

    private Hashtable<IRI, WSWRLIndividualWrapper> individuals; // Individual name -> instance

    public DefaultTargetWSWRLRuleEngine(WSWRLOntology WSWRLOntology) {
        this.wswrlOntology = WSWRLOntology;
        this.owlReasonerFactory = new StructuralReasonerFactory();

        this.individuals = new Hashtable<>();
    }

    @Override
    public void runRuleEngine() {
        try {
            this.reset();
            // Processing WSWRL rules.
            Set<WSWRLRule> wswrlRules = wswrlOntology.getWSWRLRules();
            for (WSWRLRule rule : wswrlRules) {
                if (!rule.isEnabled())
                    continue;

                Set<WSWRLAtom> body = rule.getBody();
                Set<WSWRLAtom> head = rule.getHead();

                Set<WSWRLAtom> allAtoms = new HashSet<>(body);
                allAtoms.addAll(head);

                // For each possible binding
                // Bind variables
                Set<WSWRLVariable> variables = this.getAllVariables(allAtoms);
                this.bind(variables);

                // Calculate rank weights
                rule.calculateWeights();


                // Evaluate
                float confidence = rule.calculateConfidence();
                System.out.println("Confidence : " + confidence);

                // Store results
                // Endfor
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        this.individuals = new Hashtable<>();
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

    private void bind(Set<WSWRLVariable> variables) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bind'");
    }

    // private void processOntology() throws Exception {
    // InferredOntologyGenerator iog = new
    // InferredOntologyGenerator(this.owlReasoner);
    // OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

    // iog.fillOntology(ontologyManager.getOWLDataFactory(),
    // this.wswrlOntology.getOWLOntology());

    // for (OWLAxiom axiom : this.wswrlOntology.getOWLAxioms()) {
    // if (axiom.isOfType(AxiomType.DECLARATION)) {
    // OWLEntity entity = ((OWLDeclarationAxiom) axiom).getEntity();

    // if (entity.isOWLNamedIndividual())
    // this.addIndividual((OWLNamedIndividual) entity);
    // } else if (axiom.isOfType(AxiomType.DATA_PROPERTY_ASSERTION))
    // this.handleDataPropertyAxiom((OWLDataPropertyAssertionAxiom) axiom);

    // else if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION))
    // this.handleObjectPropertyAxiom((OWLObjectPropertyAssertionAxiom) axiom);

    // else if (axiom.isOfType(AxiomType.CLASS_ASSERTION)) {
    // OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) axiom;

    // OWLNamedIndividual individual = (OWLNamedIndividual)
    // classAssertionAxiom.getIndividual();
    // this.individuals.get(individual.getIRI())
    // .addClass(classAssertionAxiom.getClassExpression().asOWLClass());
    // }
    // }
    // }

    // private void handleDataPropertyAxiom(OWLDataPropertyAssertionAxiom axiom)
    // throws Exception {
    // // Predicate and object are parsed by the wrapper.
    // WSWRLDataPropertyWrapper dataPropertyWrapper = new
    // WSWRLDataPropertyWrapper(axiom);

    // // Parse subject.
    // dataPropertyWrapper.parseSubject(this.individuals);
    // }

    // private void handleObjectPropertyAxiom(OWLObjectPropertyAssertionAxiom axiom)
    // throws Exception {
    // // Predicate is parsed by the wrapper.
    // WSWRLObjectPropertyWrapper objectPropertyWrapper = new
    // WSWRLObjectPropertyWrapper(axiom);

    // // Parse subject.
    // objectPropertyWrapper.parseSubject(this.individuals);

    // // Parse object.
    // objectPropertyWrapper.parseObject(individuals);
    // }

    // private void addIndividual(OWLNamedIndividual individual) {
    // IRI iri = individual.getIRI();
    // if (!this.individuals.containsKey(iri)) {
    // this.individuals.put(iri, new WSWRLIndividualWrapper(individual));
    // }
    // }
}
