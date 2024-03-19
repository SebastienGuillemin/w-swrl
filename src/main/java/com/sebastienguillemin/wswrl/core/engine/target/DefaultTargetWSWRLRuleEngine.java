package com.sebastienguillemin.wswrl.core.engine.target;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.WSWRLDataRangeAtom;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.engine.target.wrapper.WSWRLIndividualWrapper;

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
            this.reset();
            // Processing WSWRL rules.
            Set<WSWRLRule> wswrlRules = wswrlOntology.getWSWRLRules();
            for (WSWRLRule rule : wswrlRules) {
                Set<WSWRLAtom> body = rule.getBody();
                Set<WSWRLAtom> head = rule.getHead();

                // Get data-dependant rule predicates
                Set<WSWRLAtom> dataDependentAtoms = this.getDataDependentAtoms(body);
                
                // Get data-independent rule predicates
                Set<WSWRLAtom> dataIndependentAtoms = new HashSet<>(body);
                dataDependentAtoms.removeAll(dataDependentAtoms);

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

    private Set<WSWRLAtom> getDataDependentAtoms(Set<WSWRLAtom> atoms) {
        Set<WSWRLAtom> dataDependentAtoms = new HashSet<>();

        for (WSWRLAtom atom : atoms) {
            if (atom instanceof WSWRLDataPropertyAtom || atom instanceof WSWRLDataRangeAtom)
                dataDependentAtoms.add(atom);
        }

        return dataDependentAtoms;
    }

    private void reset() {

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
