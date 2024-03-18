package com.sebastienguillemin.wswrl.core.engine.target;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
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

public class DefaultTargetWSWRLRuleEngine extends DroolsSWRLRuleEngine implements TargetWSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;
    private Set<WSWRLRule> wswrlRules;
    private OWLReasonerFactory owlReasonerFactory;
    private OWLReasoner owlReasoner;

    private Hashtable<String, OWLClass> ontologyClasses; // Class name -> OWLClass instance.
    private Hashtable<String, OWLIndividual> ontologyIndividuals; // Individual name -> OWLIndividual instance.
    private Hashtable<String, List<OWLIndividual>> classIndividuals; // Class name -> list of OWLIndividuals.

    public DefaultTargetWSWRLRuleEngine(SWRLBridge bridge, WSWRLOntology WSWRLOntology) {
        super(bridge);
        this.wswrlOntology = WSWRLOntology;
        this.wswrlRules = wswrlOntology.getWSWRLRules();
        this.owlReasonerFactory = new StructuralReasonerFactory();
        this.owlReasoner = this.owlReasonerFactory.createReasoner(this.wswrlOntology.getOWLOntology());
        this.ontologyClasses = new Hashtable<>();
        this.ontologyIndividuals = new Hashtable<>();
        this.classIndividuals = new Hashtable<>();
    }

    @Override
    public void runRuleEngine() {
        this.processOntology();
    }

    private void processOntology() {
        InferredOntologyGenerator iog = new InferredOntologyGenerator(this.owlReasoner);
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        
        iog.fillOntology(ontologyManager.getOWLDataFactory(), this.wswrlOntology.getOWLOntology());

        for (OWLAxiom axiom : this.wswrlOntology.getOWLAxioms()) {
            if (axiom.isOfType(AxiomType.DECLARATION)) {
                OWLEntity entity = ((OWLDeclarationAxiom) axiom).getEntity();
                if (entity.isOWLClass())
                    this.ontologyClasses.put(entity.getIRI().getFragment(), (OWLClass) entity);
                else if (entity.isOWLNamedIndividual())
                    this.ontologyIndividuals.put(entity.getIRI().getFragment(), (OWLIndividual) entity);
            } else if (axiom instanceof OWLClassAssertionAxiom) {
                OWLClassAssertionAxiom assertionAxiom = (OWLClassAssertionAxiom) axiom;
                this.addIndividual(assertionAxiom.getClassExpression().asOWLClass().getIRI().getFragment(), assertionAxiom.getIndividual());
            }
        }
    }

    private void addIndividual(String className, OWLIndividual individual) {
        List<OWLIndividual> individuals = this.classIndividuals.get(className);

        if (individuals == null) {
            individuals = new ArrayList<>();
            this.classIndividuals.put(className, individuals);
        }

        individuals.add(individual);
    }
}
