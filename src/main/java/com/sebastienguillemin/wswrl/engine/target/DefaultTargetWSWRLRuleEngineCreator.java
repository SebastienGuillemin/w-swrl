package com.sebastienguillemin.wswrl.engine.target;

import com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngineCreator;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;

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