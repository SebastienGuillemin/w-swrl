package com.sebastienguillemin.wswrl.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.factory.SWRLAPIFactory;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntologyManager;
import com.sebastienguillemin.wswrl.factory.WSWRLFactory;

import lombok.Getter;

/**
 * A task taht performs an evaluation.
 */
public class EvaluationTask extends Thread {
    private static final String SWRL_RULE = ":Echantillon(?x)^:Echantillon(?y)^differentFrom(?x, ?y)^:typeDrogue(?x,?dt)^:typeDrogue(?y,?dt)^:aPrincipeActif(?x,?pax)^:aPrincipeActif(?y,?pay)^:aFormeChimique(?pax,?cf)^:aFormeChimique(?pay,?cf)^:aProduitCoupage(?x,?cp)^:aProduitCoupage(?y,?cp)^:logo(?x,?l)^:logo(?y,?l)->:estProcheDe(?x,?y)";
    private static final String WSWRL_RULE = ":Echantillon(?x)^:Echantillon(?y)^differentFrom(?x, ?y)^:typeDrogue(?x,?dt)^:typeDrogue(?y,?dt)^:aPrincipeActif(?x,?pax)^:aPrincipeActif(?y,?pay)^:aFormeChimique(?pax,?cf)^:aFormeChimique(?pay,?cf)^1*:aProduitCoupage(?x,?cp)^1*:aProduitCoupage(?y,?cp)^2*:logo(?x,?l)^2*:logo(?y,?l)->:estProcheDe(?x,?y)";

    private File ontologyFile;
    private EngineName engineName;

    @Getter
    private long executionTimeMilli;
    @Getter
    private int inferredAxiomsCount;

    /**
     * 
     * @param ontologyFile The ontology file to use for the evaluation.
     * @param engineName   The engine name.
     */
    public EvaluationTask(File ontologyFile, EngineName engineName) {
        this.ontologyFile = ontologyFile;
        this.engineName = engineName;

    }

    /**
     * Runs the evaluation task.
     */
    @Override
    public void run() {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(this.ontologyFile);
            int estProcheDeAxiomsBeforeInferring = 0;
            long start;
            switch (engineName) {
                case SWRL:
                    OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
                    OWLOntology owlOntology = owlOntologyManager.loadOntologyFromOntologyDocument(inputStream);
                    estProcheDeAxiomsBeforeInferring = this.countEstProcheDeAxioms(owlOntology);
                    SWRLRuleEngine swrlEngine = SWRLAPIFactory.createSWRLRuleEngine(owlOntology);
                    swrlEngine.createSWRLRule("SWRL Evaluation Rule", SWRL_RULE);

                    start = System.currentTimeMillis();
                    swrlEngine.infer();
                    this.executionTimeMilli = System.currentTimeMillis() - start;

                    this.inferredAxiomsCount = this.countEstProcheDeAxioms(owlOntology)
                            - estProcheDeAxiomsBeforeInferring;

                    System.out.println(String.format("---> Done: %s second(s), %s inferred axioms.",
                            (float) this.executionTimeMilli / 1000f, this.inferredAxiomsCount));
                    break;

                case WSWRL:
                    WSWRLOntologyManager wswrlOntologyManager = WSWRLFactory.createWSWRLOntologyManager();
                    WSWRLOntology wswrlOntology = wswrlOntologyManager
                            .loadWSWRLOntologyFromOntologyDocument(inputStream);

                    estProcheDeAxiomsBeforeInferring = this.countEstProcheDeAxioms(wswrlOntology.getOWLOntology());

                    WSWRLRuleEngine wswrlEngine = WSWRLFactory.createWSWRLRuleEngine(wswrlOntology);
                    System.out.println("W-SWRL rule : " + WSWRL_RULE);
                    wswrlEngine.createWSWRLRule("WSWRL Evaluation Rule", WSWRL_RULE);

                    start = System.currentTimeMillis();
                    wswrlEngine.infer();
                    this.executionTimeMilli = System.currentTimeMillis() - start;
                    wswrlOntologyManager.writeInferredAxiomsToOntology(wswrlOntology);

                    this.inferredAxiomsCount = this.countEstProcheDeAxioms(wswrlOntology.getOWLOntology())
                            - estProcheDeAxiomsBeforeInferring;

                    System.out.println(String.format("---> Done: %s second(s), %s inferred axioms.",
                            (float) this.executionTimeMilli / 1000f, this.inferredAxiomsCount));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private int countEstProcheDeAxioms(OWLOntology ontology) {
        int count = 0;
        IRI estProcheDeIRI = IRI.create("http://www.stups.fr/ontologies/2023/stups/estProcheDe");

        for (OWLAxiom axiom : ontology.getAxioms())
            if (axiom instanceof OWLObjectPropertyAssertionAxiom && ((OWLObjectPropertyAssertionAxiom) axiom)
                    .getProperty().getNamedProperty().getIRI().equals(estProcheDeIRI))
                count++;

        return count;
    }
}
