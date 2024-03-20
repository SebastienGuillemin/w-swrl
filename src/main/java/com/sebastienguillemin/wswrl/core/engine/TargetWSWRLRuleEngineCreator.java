package com.sebastienguillemin.wswrl.core.engine;

import org.swrlapi.exceptions.TargetSWRLRuleEngineException;

import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;

public interface TargetWSWRLRuleEngineCreator {
    /**
     * @return The name of the target rule engine
     */
    String getRuleEngineName();

    /**
     * @param wswrlOntology An ontology adapted for WSWRL
     * @return A target SWRL rule engine
     * @throws TargetSWRLRuleEngineException If an exception occurs during creation
     */
    TargetWSWRLRuleEngine create(WSWRLOntology WSWRLOntology);

}
