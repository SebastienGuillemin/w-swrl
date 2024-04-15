package com.sebastienguillemin.wswrl;

import java.io.IOException;
import java.io.InputStream;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntologyManager;
import com.sebastienguillemin.wswrl.factory.WSWRLFactory;
import com.sebastienguillemin.wswrl.storer.TurtlestarStorer;

public class Example {
    private static final String ONTOLOGY_FILENAME = "exampleOntology.ttl";

    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Running example program.");
        // Create an InputStream to the example ontology (placed in the
        // /src/main/resources folder).
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        // Create manager.
        WSWRLOntologyManager wswrlOntologyManager = WSWRLFactory.createWSWRLOntologyManager();

        // Create ontology
        System.out.println("--- Loading ontology from resource file: " + ONTOLOGY_FILENAME);
        InputStream inputStream = classloader.getResourceAsStream(ONTOLOGY_FILENAME);
        WSWRLOntology wswrlOntology = wswrlOntologyManager.loadWSWRLOntologyFromOntologyDocument(inputStream);
        inputStream.close();

        // Create rule engine.
        WSWRLRuleEngine wswrlRuleEngine = WSWRLFactory.createWSWRLRuleEngine(wswrlOntology);
        wswrlRuleEngine.createWSWRLRule("Example rule", ":Echantillon(?x)^:Echantillon(?y)^differentFrom(?x, ?y)^:typeDrogue(?x,?dt)^:typeDrogue(?y,?dt)^:aPrincipeActif(?x,?pax)^:aPrincipeActif(?y,?pay)^:aFormeChimique(?pax,?cf)^:aFormeChimique(?pay,?cf)^1*:aProduitCoupage(?x,?cp)^1*:aProduitCoupage(?y,?cp)^2*:logo(?x,?l)^2*:logo(?y,?l)->:estProcheDe(?x,?y)");

        // Inferring new facts.
        System.out.println("--- Inferring facts using W-SWRL rule engine, please wait.");
        wswrlRuleEngine.infer();

        // Save ontology in a Turtle-star file.
        System.out.println("\n--- Saving W-SWRL ontology in a Turtle-star file.");
        TurtlestarStorer storer = new TurtlestarStorer();
        // Uncomment the next line to show information (unsupported OWL axioms etc.).
        // storer.setMode(TurtlestarStorer.MODE.VERBOSE);
        storer.storeOntology(wswrlOntology, "result.ttls");

        // Save ontology in a Turtle file using a threshold (the cached inferred axioms
        // will be deleted).
        System.out.println("\n--- Saving W-SWRL ontology in a Turtle file.");
        wswrlOntologyManager.saveOntologyToTurtle(wswrlOntology, "result.ttl", 0.6f);
    }
}
