package com.sebastienguillemin.wswrl.core.engine;

import com.sebastienguillemin.wswrl.exception.TargetWSWRLRuleEngineException;

public interface TargetWSWRLRuleEngine {
    /**
     * Run the rule engine.
     * 
     * @throws TargetWSWRLRuleEngineException If an error occurs in the target rule
     *                                       engine
     */
    void runRuleEngine() throws TargetWSWRLRuleEngineException;
}
