package com.sebastienguillemin.wswrl.core;

import com.sebastienguillemin.wswrl.core.exception.TargetWSWRLRuleEngineException;

public interface TargetWSWRLRuleEngine {
    /**
     * Run the rule engine.
     * 
     * @throws TargetWSWRLRuleEngineException If an error occurs in the target rule
     *                                       engine
     */
    void runRuleEngine() throws TargetWSWRLRuleEngineException;
}
