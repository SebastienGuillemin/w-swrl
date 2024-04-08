package com.sebastienguillemin.wswrl.evaluation;

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
    public enum EngineName {
        SWRL, WSWRL
    }

    @Getter
    private long executionTimeMilli;

    private String ontologyFilename;
    private EngineName engineName;

    public EvaluationTask(String ontologyFilename, EngineName engineName) {
        this.ontologyFilename = ontologyFilename;
        this.engineName = engineName;

    }

    @Override
    public void run() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        InputStream inputStream = classloader.getResourceAsStream(ontologyFilename);
        try {
            long start;
            switch (engineName) {
                case SWRL:
                    OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
                    OWLOntology owlOntology = owlOntologyManager.loadOntologyFromOntologyDocument(inputStream);
                    SWRLRuleEngine swrlEngine = SWRLAPIFactory.createSWRLRuleEngine(owlOntology);
                    swrlEngine.createSWRLRule("SWRL Evaluation Rule",
                            ":Echantillon(?x)^:Echantillon(?y)^:aFormeChimique(?x,?cf)^:aFormeChimique(?y,?cf)^:typeDrogue(?x,?dt)^:typeDrogue(?y,?dt)^:aProduitCoupage(?x,?cp)^:aProduitCoupage(?y,?cp)^:logo(?x,?l)^:logo(?y,?l)->:estProcheDe(?x,?y)");

                    start = System.currentTimeMillis();
                    swrlEngine.infer();
                    this.executionTimeMilli = System.currentTimeMillis() - start;
                    System.out.println(String.format("---> Done: %s second(s), %s inferred axioms (%s in total).",
                            (float) this.executionTimeMilli / 1000f,
                            swrlEngine.getNumberOfInferredOWLAxioms(),
                            swrlEngine.getNumberOfAssertedOWLAxioms() + swrlEngine.getNumberOfInferredOWLAxioms()));
                    break;

                case WSWRL:
                    WSWRLOntologyManager wswrlOntologyManager = WSWRLFactory.createWSWRLOntologyManager();
                    WSWRLOntology wswrlOntology = wswrlOntologyManager
                            .loadWSWRLOntologyFromOntologyDocument(inputStream);
                    WSWRLRuleEngine wswrlEngine = WSWRLFactory.createWSWRLRuleEngine(wswrlOntology);
                    wswrlEngine.createWSWRLRule("WSWRL Evaluation Rule",
                            "0*:Echantillon(?x)^0*:Echantillon(?y)^0*:aFormeChimique(?x,?cf)^0*:aFormeChimique(?y,?cf)^0*:typeDrogue(?x,?dt)^0*:typeDrogue(?y,?d)^1*:aProduitCoupage(?x,?cp)^1*:aProduitCoupage(?y,?c)^2*:logo(?x,?l)^2*:logo(?y,?l)->:estProcheDe(?x,?y)");
                    // wswrlEngine.createWSWRLRule("test", "concept1(?x)^concept1(?y)^data(?x, ?z)->data(?x,?z)");

                    start = System.currentTimeMillis();
                    wswrlEngine.infer();
                    this.executionTimeMilli = System.currentTimeMillis() - start;
                    System.out.println(String.format("---> Done: %s second(s), %s inferred axioms (%s in total).",
                            (float) this.executionTimeMilli / 1000f,
                            wswrlEngine.getNumberOfInferredOWLAxioms(),
                            wswrlEngine.getNumberOfAssertedOWLAxioms() + wswrlEngine.getNumberOfInferredOWLAxioms()));
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
