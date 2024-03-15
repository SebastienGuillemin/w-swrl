package com.sebastienguillemin.wswrl;

import java.io.IOException;
import java.io.InputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.factory.WSWRLFactory;

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

        WSWRLRule rule = wswrlRuleEngine.createWSWRLRule("testRule", "1*concept1(?x)->concept2(?x)");

        for (WSWRLAtom atom : rule.getBody())
            System.out.println(atom);

        for (WSWRLAtom atom : rule.getHead())
            System.out.println(atom);

        // swrlRuleEngine.infer();

        // Set<SWRLAPIRule> swrlRules = swrlRuleEngine.getSWRLRules();

        // for (SWRLAPIRule rule : swrlRules) {
        // System.out.println("Body :");

        // for (SWRLAtom atom : rule.getBody()) {
        // System.out.println(" " + atom.getPredicate());

        // for (SWRLArgument argument : atom.getAllArguments()) {
        // System.out.println(" " + argument);
        // System.out.println(" " + ((SWRLVariable) argument).isIndividual());
        // }

        // System.out.println();
        // }

        // }
    }
}