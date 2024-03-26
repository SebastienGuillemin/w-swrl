package com.sebastienguillemin.wswrl.core.engine;

import com.sebastienguillemin.wswrl.exception.TargetWSWRLRuleEngineException;

/**
 * The WSWRL target rule engine interface inspired by {@link org.swrlapi.bridge.TargetSWRLRuleEngine}.
 * A target rule engine is used whithin a rule engine.
 * 
 * @see com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine
 * 
 */
public interface TargetWSWRLRuleEngine {
    /**
     * Run the rule engine.
     * 
     * @throws TargetWSWRLRuleEngineException If an error occurs in the target rule
     *                                       engine.
     */
    void runRuleEngine() throws TargetWSWRLRuleEngineException;
}
