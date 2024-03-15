package com.sebastienguillemin.wswrl.core.engine;

import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLRuleEngineException;

public class DefaultWSWRLRuleEngine implements WSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;

    public DefaultWSWRLRuleEngine(WSWRLOntology ontology) {
        this.wswrlOntology = ontology;
    }

    @Override
    public void infer() throws WSWRLRuleEngineException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createWSWRLRule'");
    }

    @Override
    public WSWRLRule createWSWRLRule(String ruleName, String rule) throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException {
        return this.wswrlOntology.createWSWRLRule(ruleName, rule);
    }

    @Override
    public WSWRLRule createSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException {
                return this.wswrlOntology.createWSWRLRule(ruleName, rule, comment, isActive);
    }

}