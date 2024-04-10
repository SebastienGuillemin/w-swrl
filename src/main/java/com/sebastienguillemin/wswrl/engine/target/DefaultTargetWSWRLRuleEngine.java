package com.sebastienguillemin.wswrl.engine.target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
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
import com.sebastienguillemin.wswrl.core.rule.WSWRLRuleResult;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.binding.VariableBinding;
import com.sebastienguillemin.wswrl.rule.DefaultWSWRLIndividual;
import com.sebastienguillemin.wswrl.rule.variable.binding.DefaultVariableBinding;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

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
        long cumulativeNewBindingCalculationTime = 0, cumulativeConfidenceCalculationTime = 0,
                cumulativeWeightCalculationTime = 0, cumulativeAxiomsInsertionTime = 0;
        long start, check1, check2, check3, check4;
        try {
            this.reset();
            this.processOntology();

            // Processing WSWRL rules.
            Set<WSWRLRule> wswrlRules = wswrlOntology.getWSWRLRules();
            VariableBinding binding;
            int newFactsCounter = 0;
            int skipped = 0;
            List<WSWRLRuleResult> results = new ArrayList<>();

            ProgressBarBuilder pbb = new ProgressBarBuilder()
                    .setTaskName("Testing binding possibilities.")
                    .hideEta()
                    .setStyle(ProgressBarStyle.UNICODE_BLOCK);

            for (WSWRLRule rule : wswrlRules) {
                if (!rule.isEnabled())
                    continue;

                Set<WSWRLAtom> body = rule.getBody();
                binding = new DefaultVariableBinding(body, this.individuals, this.classToIndividuals);

                try (ProgressBar pb2 = pbb.setInitialMax(binding.getBindingPossibilities()).build()) {
                    while (binding.hasNext()) {
                        pb2.step();
                        start = System.currentTimeMillis();
                        binding.nextBinding();
                        check1 = System.currentTimeMillis();

                        // Calculate rank weights
                        boolean skip = rule.calculateWeights();
                        check2 = System.currentTimeMillis();
                        if (skip) {
                            skipped++;
                            continue;
                        }

                        // Evaluate
                        float confidence = rule.calculateConfidence();
                        check3 = System.currentTimeMillis();

                        // Store result
                        if (confidence > 0) {
                            // System.out.println("New fact for: " + binding + " confidence : " +
                            // confidence);
                            newFactsCounter++;
                            // this.wswrlOntology.addWSWRLInferredAxiom(rule.getHead(), confidence);
                        }

                        cumulativeNewBindingCalculationTime += (check1 - start);
                        cumulativeWeightCalculationTime += (check2 - check1);
                        cumulativeConfidenceCalculationTime += (check3 - check2);

                    }
                }
            }
            System.out.println(newFactsCounter + " new facts inferred with WSWRL rules.");
            // Adding resulsts
            // check3 = System.currentTimeMillis();
            // this.wswrlOntology.addWSWRLInferredAxiom(results);
            // check4 = System.currentTimeMillis();
            // cumulativeAxiomsInsertionTime += (check4 - check3);

            System.out.println("New facts count: " + newFactsCounter);
            System.out.println("Skipped: " + skipped);
            System.out.println(
                    "cumulative New Binding Calculation Time = "
                            + ((float) cumulativeNewBindingCalculationTime / 1000.0f) +
                            // "\n ---> nextIndividualsBinding: " + ((float)
                            // DefaultVariableBinding.nextIndividualsBinding / 1000.0f) +
                            // "\n ---> bindIndividuals: " + ((float) DefaultVariableBinding.bindIndividuals
                            // / 1000.0f) +
                            // "\n ---> processDataProperties: " + ((float)
                            // DefaultVariableBinding.processDataProperties / 1000.0f) +
                            // "\n ---> nextDataBinding: " + ((float) DefaultVariableBinding.nextDataBinding
                            // / 1000.0f) +
                            // "\n ---> bindDataVariables: " + ((float)
                            // DefaultVariableBinding.bindDataVariables / 1000.0f) +
                            "\n\ncumulative Weight Calculation Time = "
                            + ((float) cumulativeWeightCalculationTime / 1000.0f) +
                            "\ncumulative Confidence Calculation Time = "
                            + ((float) cumulativeConfidenceCalculationTime / 1000.0f)
            // "\ncumulative Axioms Insertion Time = " + ((float)
            // cumulativeAxiomsInsertionTime / 1000.0f)
            );

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
