package com.sebastienguillemin.wswrl;

import java.io.IOException;
import java.io.InputStream;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntologyManager;
import com.sebastienguillemin.wswrl.factory.WSWRLFactory;
import com.sebastienguillemin.wswrl.storer.TurtlestarStorer;

public class Example {
    public static void main(String[] args) throws IOException, Exception {
        // Create an InputStream to the example ontology.
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("exampleontology.ttl");

        // Create ontology manager and ontology.
        WSWRLOntologyManager ontologyManager = WSWRLFactory.createWSWRLOntologyManager();
        WSWRLOntology ontology = ontologyManager.loadWSWRLOntologyFromOntologyDocument(inputStream);
        inputStream.close();

        // Create WSWRL rule engine.
        WSWRLRuleEngine wswrlRuleEngine = WSWRLFactory.createWSWRLRuleEngine(ontology);

        // Create WSWRL rule.
        wswrlRuleEngine.createWSWRLRule("test", "concept1(?x)^1*concept1(?y)^differentFrom(?x,?y) -> linked(?x, ?y)");

        // Inferring new facts.
        wswrlRuleEngine.infer();

        // Save ontology in a Turtle-star file.
        TurtlestarStorer storer = new TurtlestarStorer();
        storer.storeOntology(ontology, "result.ttl");

        // Save ontology in a Turtle file using a threshold (the cached inferred axioms will be deleted).
        ontologyManager.saveOntologyToTurtle(ontology, "result.ttl", 0.6f);
    }
}
