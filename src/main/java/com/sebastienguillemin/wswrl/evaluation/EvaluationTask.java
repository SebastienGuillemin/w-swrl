package com.sebastienguillemin.wswrl.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.factory.SWRLAPIFactory;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntologyManager;
import com.sebastienguillemin.wswrl.factory.WSWRLFactory;

import lombok.Getter;

public class EvaluationTask extends Thread {
    private static final String SWRL_RULE = ":Echantillon(?x)^:Echantillon(?y)^:aFormeChimique(?x,?cf)^:aFormeChimique(?y,?cf)^:typeDrogue(?x,?dt)^:typeDrogue(?y,?dt)^:aProduitCoupage(?x,?cp)^:aProduitCoupage(?y,?cp)^:logo(?x,?l)^:logo(?y,?l)->:estProcheDe(?x,?y)";
    private static final String WSWRL_RULE = "0*:Echantillon(?x)^0*:Echantillon(?y)^1*:aFormeChimique(?x,?cf)^1*:aFormeChimique(?y,?cf)^1*:typeDrogue(?x,?dt)^1*:typeDrogue(?y,?dt)^2*:aProduitCoupage(?x,?cp)^2*:aProduitCoupage(?y,?cp)^3*:logo(?x,?l)^3*:logo(?y,?l)->:estProcheDe(?x,?y)";

    private File ontologyFile;
    private EngineName engineName;

    @Getter
    private long executionTimeMilli;
    @Getter
    private int inferredAxiomsCount;

    public EvaluationTask(File ontologyFile, EngineName engineName) {
        this.ontologyFile = ontologyFile;
        this.engineName = engineName;

    }

    @Override
    public void run() {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(this.ontologyFile);
            long start;
            switch (engineName) {
                case SWRL:
                    OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
                    OWLOntology owlOntology = owlOntologyManager.loadOntologyFromOntologyDocument(inputStream);
                    SWRLRuleEngine swrlEngine = SWRLAPIFactory.createSWRLRuleEngine(owlOntology);
                    swrlEngine.createSWRLRule("SWRL Evaluation Rule", SWRL_RULE);

                    start = System.currentTimeMillis();
                    swrlEngine.infer();
                    this.executionTimeMilli = System.currentTimeMillis() - start;

                    this.inferredAxiomsCount = swrlEngine.getNumberOfAssertedOWLAxioms()
                            + swrlEngine.getNumberOfInferredOWLAxioms();

                    System.out.println(String.format("---> Done: %s second(s), %s inferred axioms (%s in total).",
                            (float) this.executionTimeMilli / 1000f,
                            swrlEngine.getNumberOfInferredOWLAxioms(),
                            this.inferredAxiomsCount));
                    break;

                case WSWRL:
                    WSWRLOntologyManager wswrlOntologyManager = WSWRLFactory.createWSWRLOntologyManager();
                    WSWRLOntology wswrlOntology = wswrlOntologyManager
                            .loadWSWRLOntologyFromOntologyDocument(inputStream);
                    WSWRLRuleEngine wswrlEngine = WSWRLFactory.createWSWRLRuleEngine(wswrlOntology);
                    wswrlEngine.createWSWRLRule("WSWRL Evaluation Rule", WSWRL_RULE);


                    start = System.currentTimeMillis();
                    wswrlEngine.infer();
                    this.executionTimeMilli = System.currentTimeMillis() - start;
                    
                    this.inferredAxiomsCount = wswrlEngine.getNumberOfAssertedOWLAxioms()
                            + wswrlEngine.getNumberOfInferredOWLAxioms();

                    System.out.println(String.format("---> Done: %s second(s), %s inferred axioms (%s in total).",
                            (float) this.executionTimeMilli / 1000f,
                            wswrlEngine.getNumberOfInferredOWLAxioms(),
                            this.inferredAxiomsCount));
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
}
