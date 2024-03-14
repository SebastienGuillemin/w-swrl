package com.sebastienguillemin.wswrl.core.factory;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLRuleEngineException;
import org.swrlapi.factory.SWRLAPIFactory;

import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.engine.DefaultWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.DefaultWSWRLOntology;

public class WSWRLFactory extends SWRLAPIFactory {

    /**
     * @param ontology An OWL ontology
     * @return A SWRL rule engine
     * @throws SWRLRuleEngineException If an error occurs during rule engine
     *                                 creation
     */
    public static WSWRLRuleEngine createWSWRLRuleEngine(OWLOntology ontology) {
        IRIResolver iriResolver = createIRIResolver();
        WSWRLOntology wswrlOntology = new DefaultWSWRLOntology(ontology, iriResolver);

        return new DefaultWSWRLRuleEngine(wswrlOntology);
    }
}
