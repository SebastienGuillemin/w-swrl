package com.sebastienguillemin.wswrl.engine.target;

import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngineCreator;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;

public class DefaultTargetWSWRLRuleEngineCreator implements TargetWSWRLRuleEngineCreator {

    @Override
    public String getRuleEngineName() {
        return "WSWRL Rule Engine";
    }

    @Override
    public TargetWSWRLRuleEngine create(WSWRLOntology WSWRLOntology) {
        return new DefaultTargetWSWRLRuleEngine(WSWRLOntology);
    }
}