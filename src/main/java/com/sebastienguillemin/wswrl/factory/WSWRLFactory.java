package com.sebastienguillemin.wswrl.factory;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.exceptions.SWRLRuleEngineException;
import org.swrlapi.factory.SWRLAPIFactory;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.factory.WSWRLRuleEngineFactory;

/**
 * General static factory.
 */
public class WSWRLFactory extends SWRLAPIFactory {
    private static final WSWRLRuleEngineFactory WSWRL_RULE_ENGINE_FACTORY;

    static {
        WSWRL_RULE_ENGINE_FACTORY = WSWRLInternalFactory.getWSWRLRuleEngineFactory();
    }

    /**
     * @param ontology An OWL ontology
     * @return A SWRL rule engine
     * @throws SWRLBuiltInException 
     * @throws SWRLRuleEngineException If an error occurs during rule engine
     *                                 creation
     */
    public static WSWRLRuleEngine createWSWRLRuleEngine(OWLOntology ontology) throws SWRLBuiltInException {
        IRIResolver iriResolver = createIRIResolver();

        return WSWRL_RULE_ENGINE_FACTORY.createWSWRLRuleEngine(ontology, iriResolver);
    }
}
