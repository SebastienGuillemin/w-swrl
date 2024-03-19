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
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.drools.core.DroolsSWRLRuleEngine;

import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.engine.target.wrapper.WSWRLDataPropertyWrapper;
import com.sebastienguillemin.wswrl.core.engine.target.wrapper.WSWRLIndividualWrapper;
import com.sebastienguillemin.wswrl.core.engine.target.wrapper.WSWRLObjectPropertyWrapper;

public class DefaultTargetWSWRLRuleEngine extends DroolsSWRLRuleEngine implements TargetWSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;
    private Set<WSWRLRule> wswrlRules;
    private OWLReasonerFactory owlReasonerFactory;
    private OWLReasoner owlReasoner;

    private Hashtable<IRI, WSWRLIndividualWrapper> individuals; // Individual name -> instance

    public DefaultTargetWSWRLRuleEngine(SWRLBridge bridge, WSWRLOntology WSWRLOntology) {
        super(bridge);
        this.wswrlOntology = WSWRLOntology;
        this.wswrlRules = wswrlOntology.getWSWRLRules();
        this.owlReasonerFactory = new StructuralReasonerFactory();
        this.owlReasoner = this.owlReasonerFactory.createReasoner(this.wswrlOntology.getOWLOntology());

        this.individuals = new Hashtable<>();
    }

    @Override
    public void runRuleEngine() {
        try {
            this.processOntology();

            for (WSWRLIndividualWrapper individual : this.individuals.values()) {
                System.out.println(individual);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // for (WSWRLRule rule : this.wswrlRules) {
        // DD <- Get data-dependant rule predicates
        // DI <- Get data-independent rule predicates

        // Bind variables for DI
        // Test valuability for DD

        // Compute rank weights
        // Bind variable for DD

        // Evaluate
        // Store results
        // }
    }

    private void processOntology() throws Exception {
        InferredOntologyGenerator iog = new InferredOntologyGenerator(this.owlReasoner);
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

        iog.fillOntology(ontologyManager.getOWLDataFactory(), this.wswrlOntology.getOWLOntology());

        for (OWLAxiom axiom : this.wswrlOntology.getOWLAxioms()) {
            System.out.println("--> " + axiom);
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
