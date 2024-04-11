package com.sebastienguillemin.wswrl.engine.target;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
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
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.binding.VariableBinding;
import com.sebastienguillemin.wswrl.rule.DefaultWSWRLIndividual;
import com.sebastienguillemin.wswrl.rule.variable.binding.DefaultVariableBinding;

/**
 * {@inheritDoc}
 */
public class DefaultTargetWSWRLRuleEngine implements TargetWSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;
    private Hashtable<IRI, WSWRLIndividual> individuals; // Individual IRI -> individual.
    private Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals; // Class IRI -> individuals.

    /**
     * Constructor.
     * 
     * @param WSWRLOntology The ontology to used to create the target rule engine.
     */
    public DefaultTargetWSWRLRuleEngine(WSWRLOntology WSWRLOntology) {
        this.wswrlOntology = WSWRLOntology;
        this.individuals = new Hashtable<>();
        this.classToIndividuals = new Hashtable<>();
    }

    @Override
    public void runRuleEngine() {
        try {
            this.reset();
            this.processOntology();

            // Processing WSWRL rules.
            Set<WSWRLRule> wswrlRules = wswrlOntology.getWSWRLRules();
            VariableBinding binding;

            for (WSWRLRule rule : wswrlRules) {
                if (!rule.isEnabled())
                    continue;

                Set<WSWRLAtom> body = rule.getBody();
                binding = new DefaultVariableBinding(body, this.individuals, this.classToIndividuals);
                while (binding.hasNext()) {
                    binding.nextBinding();

                    // Calculate rank weights
                    boolean skip = rule.calculateWeights();

                    if (skip) {
                        continue;
                    }

                    // Evaluate
                    float confidence = rule.calculateConfidence();

                    // Store result
                    if (confidence > 0) {
                        this.wswrlOntology.addWSWRLInferredAxiom(rule.getHead(), confidence);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        this.individuals.clear();
        this.classToIndividuals.clear();
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
                this.addIndividual(wswrlIndividual);
                this.addindividualToClass(wswrlIndividual);
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

    private void addindividualToClass(WSWRLIndividual individual) {
        Set<WSWRLIndividual> classIndividuals;
        IRI classIRI;
        for (OWLClass owlClass : individual.getOWLClasses()) {
            classIRI = owlClass.getIRI();
            if (!this.classToIndividuals.containsKey(classIRI)) {
                classIndividuals = new HashSet<>();
                this.classToIndividuals.put(classIRI, classIndividuals);
            } else
                classIndividuals = this.classToIndividuals.get(classIRI);

            classIndividuals.add(individual);
        }

    }
}
