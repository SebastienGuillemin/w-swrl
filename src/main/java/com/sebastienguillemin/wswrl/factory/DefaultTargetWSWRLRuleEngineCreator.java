package com.sebastienguillemin.wswrl.factory;

import com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.factory.TargetWSWRLRuleEngineCreator;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.engine.target.DefaultTargetWSWRLRuleEngine;

/**
 * {@inheritDoc}
 */
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