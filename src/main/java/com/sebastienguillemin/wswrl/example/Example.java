package com.sebastienguillemin.wswrl.example;

import java.io.InputStream;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntologyManager;
import com.sebastienguillemin.wswrl.factory.WSWRLFactory;
import com.sebastienguillemin.wswrl.storer.TurtlestarStorer;

/**
 * Example program.
 */
public class Example {
    private static final String ONTOLOGY_FILENAME = "exampleOntology.ttl";

    /**
     * 
     * Start the example program :<br>
     * 1) load the '/src/amin.resources/exampleOntology.ttl' example ontology.<br>
     * 2) infer new facts over the ontology using a WSWRL rule :<br>
     * {@code :Echantillon(?x)^:Echantillon(?y)^differentFrom(?x, ?y)^:typeDrogue(?x,?dt)^:typeDrogue(?y,?dt)^:aPrincipeActif(?x,?pax)^:aPrincipeActif(?y,?pay)^:aFormeChimique(?pax,?cf)^:aFormeChimique(?pay,?cf)^1*:aProduitCoupage(?x,?cp)^1*:aProduitCoupage(?y,?cp)^2*:logo(?x,?l)^2*:logo(?y,?l)->:estProcheDe(?x,?y)}<br>
     * 3) store the result in a Turtle* file.<br>
     * 4) store the result in a Turtle file using 0.8 as threshold.
     * 
     * 
     * @param args No args need to be passed to the main method.
     * @throws Exception If an exception occurs while running the example program.
     */
    public static void main(String[] args) throws Exception {
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

        // Create W-SWRL rule engine and example rule.
        WSWRLRuleEngine wswrlRuleEngine = WSWRLFactory.createWSWRLRuleEngine(wswrlOntology);
        wswrlRuleEngine.createWSWRLRule("Example rule",
                ":Echantillon(?x)^:Echantillon(?y)^differentFrom(?x, ?y)^:typeDrogue(?x,?dt)^:typeDrogue(?y,?dt)^:aPrincipeActif(?x,?pax)^:aPrincipeActif(?y,?pay)^:aFormeChimique(?pax,?cf)^:aFormeChimique(?pay,?cf)^1*:aProduitCoupage(?x,?cp)^1*:aProduitCoupage(?y,?cp)^2*:logo(?x,?l)^2*:logo(?y,?l)->:estProcheDe(?x,?y)");

        // Inferring new facts.
        System.out.println("--- Inferring facts using W-SWRL rule engine, please wait.");
        wswrlRuleEngine.infer();

        // Save ontology in a Turtle-star file.
        System.out.println("--- Saving W-SWRL ontology in a Turtle-star file.");
        TurtlestarStorer storer = new TurtlestarStorer();
        // Uncomment the next line to show information (unsupported OWL axioms etc.).
        // storer.setMode(TurtlestarStorer.MODE.VERBOSE);
        storer.storeOntology(wswrlOntology, "result.ttls");

        // Save ontology in a Turtle file using a threshold (the cached inferred axioms
        // will be deleted).
        System.out.println("--- Saving W-SWRL ontology in a Turtle file.");
        wswrlOntologyManager.saveOntologyToTurtle(wswrlOntology, "result.ttl", 0.8f);
    }
}
