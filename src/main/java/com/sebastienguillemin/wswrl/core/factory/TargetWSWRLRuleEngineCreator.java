package com.sebastienguillemin.wswrl.core.factory;

import org.swrlapi.exceptions.TargetSWRLRuleEngineException;

import com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;

/**
 * A creator for WSRL target rule engine. Inspired by
 * {@link org.swrlapi.bridge.TargetSWRLRuleEngineCreator}.
 */
public interface TargetWSWRLRuleEngineCreator {
    /**
     * Returns the target rule engine name.
     * 
     * @return The name of the target rule engine
     */
    String getRuleEngineName();

    /**
     * Creates a {@link org.swrlapi.exceptions.TargetSWRLRuleEngineException} for a
     * given {@link com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology}.
     * 
     * @param WSWRLOntology   An WSWRL ontology.
     * @return A target SWRL rule engine.
     * @throws TargetSWRLRuleEngineException If an exception occurs during creation.
     */
    TargetWSWRLRuleEngine create(WSWRLOntology WSWRLOntology);

}
