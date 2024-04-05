package com.sebastienguillemin.wswrl;

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
import com.sebastienguillemin.wswrl.storer.TurtlestarStorer;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;

public class Example {
    private static final String ONTOLOGY_FILENAME = "exampleOntology.ttl";

    private static void infer(SWRLRuleEngine engine, ProgressBarBuilder pbb) {
        float inferringTime;
        try (ProgressBar pb = pbb.build()) {
            engine.infer();
            pb.step();
            inferringTime = (float) pb.getTotalElapsed().toMillis();
        }

        System.out.println(" |--> Inferring time : " + (inferringTime / 1000.0f) + " second(s).");
    }

    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Running example program.");
        // Create an InputStream to the example ontology (placed in the
        // /src/main/resources folder).
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        // // Create managers
        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
        WSWRLOntologyManager wswrlOntologyManager = WSWRLFactory.createWSWRLOntologyManager();

        // Create ontologies (need to create two instances in order to ensure test
        // independance)
        System.out.println("--- Loading ontology from resource file: " +
                ONTOLOGY_FILENAME);
        InputStream inputStream = classloader.getResourceAsStream(ONTOLOGY_FILENAME);
        OWLOntology swrlapiowlOntology = owlOntologyManager.loadOntologyFromOntologyDocument(inputStream);
        inputStream.close();

        inputStream = classloader.getResourceAsStream(ONTOLOGY_FILENAME);
        WSWRLOntology wswrlOntology = wswrlOntologyManager.loadWSWRLOntologyFromOntologyDocument(inputStream);
        inputStream.close();

        // Create rule engines.
        SWRLRuleEngine swrlRuleEngine = SWRLAPIFactory.createSWRLRuleEngine(swrlapiowlOntology);
        WSWRLRuleEngine wswrlRuleEngine = WSWRLFactory.createWSWRLRuleEngine(wswrlOntology);

        // Inferring new facts using SWRLAPI engine.
        ProgressBarBuilder pbb = ProgressBar.builder()
                .setInitialMax(1)
                .setTaskName("--- Inferring facts using SWRLAPI rule engine - Progress :")
                .continuousUpdate()
                .hideEta()
                .setUpdateIntervalMillis(1000);

        infer(swrlRuleEngine, pbb);

        // Inferring new facts using W-SWRL engine.
        pbb = pbb.setTaskName("--- Inferring facts using W-SWRL rule engine - Progress :");
        infer(wswrlRuleEngine, pbb);

        // Save ontology in a Turtle-star file.
        System.out.println("\n--- Saving W-SWRL ontology in a Turtle-star file.");
        TurtlestarStorer storer = new TurtlestarStorer();
        // Uncomment this line to show information (unsupported OWL axioms etc.).
        // storer.setMode(TurtlestarStorer.MODE.VERBOSE);
        storer.storeOntology(wswrlOntology, "result.ttls");

        // Save ontology in a Turtle file using a threshold (the cached inferred axioms
        // will be deleted).
        System.out.println("\n--- Saving W-SWRL ontology in a Turtle file.");
        wswrlOntologyManager.saveOntologyToTurtle(wswrlOntology, "result.ttl", 0.6f);
    }
}
