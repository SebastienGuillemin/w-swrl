package com.sebastienguillemin.wswrl.core;

import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.exceptions.TargetSWRLRuleEngineException;

public interface TargetWSWRLRuleEngineCreator {
    /**
     * @return The name of the target rule engine
     */
    String getRuleEngineName();

    /**
     * @param bridge A SWRL rule engine bridge associated with the engine
     * @param wswrlOntology An ontology adapted for WSWRL
     * @return A target SWRL rule engine
     * @throws TargetSWRLRuleEngineException If an exception occurs during creation
     */
    TargetWSWRLRuleEngine create(SWRLBridge bridge, WSWRLOntology WSWRLOntology);

}
