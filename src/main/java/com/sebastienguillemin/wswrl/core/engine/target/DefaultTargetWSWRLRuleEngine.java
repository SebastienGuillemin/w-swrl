package com.sebastienguillemin.wswrl.core.engine.target;

import java.util.Hashtable;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
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
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;

import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.engine.target.wrapper.WSWRLDataPropertyWrapper;
import com.sebastienguillemin.wswrl.core.engine.target.wrapper.WSWRLIndividualWrapper;
import com.sebastienguillemin.wswrl.core.engine.target.wrapper.WSWRLObjectPropertyWrapper;

public class DefaultTargetWSWRLRuleEngine implements TargetWSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;
    private OWLReasonerFactory owlReasonerFactory;
    private OWLReasoner owlReasoner;

    private Hashtable<IRI, WSWRLIndividualWrapper> individuals; // Individual name -> instance

    public DefaultTargetWSWRLRuleEngine(WSWRLOntology WSWRLOntology) {
        this.wswrlOntology = WSWRLOntology;
        this.owlReasonerFactory = new StructuralReasonerFactory();
        this.owlReasoner = this.owlReasonerFactory.createReasoner(this.wswrlOntology.getOWLOntology());

        this.individuals = new Hashtable<>();
    }

    @Override
    public void runRuleEngine() {
        try {
            // Processing WSWRL rules.
            Set<WSWRLRule> wswrlRules = wswrlOntology.getWSWRLRules();
            for (WSWRLRule rule : wswrlRules) {
                Set<WSWRLAtom> body = rule.getBody();
                Set<WSWRLAtom> head = rule.getHead();

                // DD <- Get data-dependant rule predicates
                // for (WSWRLAtom atom : body) {
                //     System.err.println(atom);
                // }
                // DI <- Get data-independent rule predicates

                // Bind variables for DI
                // Test valuability for DD

                // Compute rank weights
                // Bind variable for DD

                // Evaluate
                // Store results
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processOntology() throws Exception {
        InferredOntologyGenerator iog = new InferredOntologyGenerator(this.owlReasoner);
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

        iog.fillOntology(ontologyManager.getOWLDataFactory(), this.wswrlOntology.getOWLOntology());

        for (OWLAxiom axiom : this.wswrlOntology.getOWLAxioms()) {
            if (axiom.isOfType(AxiomType.DECLARATION)) {
                OWLEntity entity = ((OWLDeclarationAxiom) axiom).getEntity();

                if (entity.isOWLNamedIndividual())
                    this.addIndividual((OWLNamedIndividual) entity);
            } else if (axiom.isOfType(AxiomType.DATA_PROPERTY_ASSERTION))
                this.handleDataPropertyAxiom((OWLDataPropertyAssertionAxiom) axiom);

            else if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION))
                this.handleObjectPropertyAxiom((OWLObjectPropertyAssertionAxiom) axiom);

            else if (axiom.isOfType(AxiomType.CLASS_ASSERTION)) {
                OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) axiom;

                OWLNamedIndividual individual = (OWLNamedIndividual) classAssertionAxiom.getIndividual();
                this.individuals.get(individual.getIRI())
                        .addClass(classAssertionAxiom.getClassExpression().asOWLClass());
            }
        }
    }

    private void handleDataPropertyAxiom(OWLDataPropertyAssertionAxiom axiom) throws Exception {
        // Predicate and object are parsed by the wrapper.
        WSWRLDataPropertyWrapper dataPropertyWrapper = new WSWRLDataPropertyWrapper(axiom);

        // Parse subject.
        dataPropertyWrapper.parseSubject(this.individuals);
    }

    private void handleObjectPropertyAxiom(OWLObjectPropertyAssertionAxiom axiom) throws Exception {
        // Predicate is parsed by the wrapper.
        WSWRLObjectPropertyWrapper objectPropertyWrapper = new WSWRLObjectPropertyWrapper(axiom);

        // Parse subject.
        objectPropertyWrapper.parseSubject(this.individuals);

        // Parse object.
        objectPropertyWrapper.parseObject(individuals);
    }

    private void addIndividual(OWLNamedIndividual individual) {
        IRI iri = individual.getIRI();
        if (!this.individuals.containsKey(iri)) {
            this.individuals.put(iri, new WSWRLIndividualWrapper(individual));
        }
    }
}
