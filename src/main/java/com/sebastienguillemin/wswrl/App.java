package com.sebastienguillemin.wswrl;

import java.io.IOException;
import java.io.InputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.factory.WSWRLFactory;

/**
 * *TO DO : delete when finish*
 *
 */
public class App {
    public static void main(String[] args) throws IOException, Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("testontology.ttl");

        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(inputStream);

        WSWRLRuleEngine wswrlRuleEngine = WSWRLFactory.createWSWRLRuleEngine(ontology);
        wswrlRuleEngine.createWSWRLRule("test", "xsd::string(?d)^dataprop(?x,?d) -> concept1(?x)");
        
        wswrlRuleEngine.infer();
    }
}