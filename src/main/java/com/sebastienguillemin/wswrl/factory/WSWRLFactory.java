package com.sebastienguillemin.wswrl.factory;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.exceptions.SWRLRuleEngineException;
import org.swrlapi.factory.SWRLAPIFactory;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.factory.WSWRLRuleEngineFactory;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntologyManager;
import com.sebastienguillemin.wswrl.ontology.DefaultWSWRLOntologyManager;

/**
 * General static factory.
 */
public class WSWRLFactory extends SWRLAPIFactory {
    private static final WSWRLRuleEngineFactory WSWRL_RULE_ENGINE_FACTORY;

    static {
        WSWRL_RULE_ENGINE_FACTORY = WSWRLInternalFactory.getWSWRLRuleEngineFactory();
    }

    /**
     * @param ontology An OWL ontology.
     * 
     * @return A SWRL rule engine
     * 
     * @throws SWRLBuiltInException 
     * @throws SWRLRuleEngineException If an error occurs during rule engine
     *                                 creation
     */
    public static WSWRLRuleEngine createWSWRLRuleEngine(WSWRLOntology ontology) throws SWRLBuiltInException {
        return WSWRL_RULE_ENGINE_FACTORY.createWSWRLRuleEngine(ontology);
    }

    public static WSWRLOntologyManager createWSWRLOntologyManager() {
        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
        return new DefaultWSWRLOntologyManager(owlOntologyManager);
    }
}
