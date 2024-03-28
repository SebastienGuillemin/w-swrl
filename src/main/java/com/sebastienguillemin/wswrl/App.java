package com.sebastienguillemin.wswrl;

import java.io.IOException;
import java.io.InputStream;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntologyManager;
import com.sebastienguillemin.wswrl.factory.WSWRLFactory;

/**
 * *TO DO : delete when finish*
 *
 */
public class App {
    public static void main(String[] args) throws IOException, Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("testontology.ttl");

        WSWRLOntologyManager ontologyManager = WSWRLFactory.createWSWRLOntologyManager();
        WSWRLOntology ontology = ontologyManager.loadWSWRLOntologyFromOntologyDocument(inputStream);
        inputStream.close();
        
        WSWRLRuleEngine wswrlRuleEngine = WSWRLFactory.createWSWRLRuleEngine(ontology);
        wswrlRuleEngine.createWSWRLRule("test", "concept1(?x)^1*concept1(?y)^differentFrom(?x,?y) -> linked(?x, ?y)");
        
        wswrlRuleEngine.infer();

        ontologyManager.saveOntologyToTurtle(ontology, "result.ttl", 0.6f);
    }
}