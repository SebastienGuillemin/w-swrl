package com.sebastienguillemin.wswrl.core.engine;

import java.util.Hashtable;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.factory.DefaultSWRLRuleAndQueryEngineFactory;

import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLRuleEngineException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLRuleException;

public class DefaultWSWRLRuleEngine extends DefaultSWRLRuleEngine implements WSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;
    private Hashtable<String, WSWRLRule> wswrlRules;

    public DefaultWSWRLRuleEngine(WSWRLOntology ontology) {
        this.wswrlOntology = ontology;
        this.wswrlRules = new Hashtable<>();
    }

    @Override
    public void infer() throws WSWRLRuleEngineException {
        throw new UnsupportedOperationException("Unimplemented method 'createWSWRLRule'");
    }

    @Override
    public WSWRLRule createWSWRLRule(String ruleName, String rule) throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException {
        WSWRLRule wswrlRule = this.wswrlOntology.createWSWRLRule(ruleName, rule);
        this.wswrlRules.put(ruleName, wswrlRule);
        return wswrlRule;
    }
    
    @Override
    public WSWRLRule createSWRLRule(String ruleName, String rule, String comment, boolean isActive) throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException {
        WSWRLRule wswrlRule = this.wswrlOntology.createWSWRLRule(ruleName, rule, comment, isActive);
        this.wswrlRules.put(ruleName, wswrlRule);
        return wswrlRule;
    }

    @Override
    public Hashtable<String, WSWRLRule> getWSWRLRules() {
        return this.wswrlRules;
    }

    @Override
    public WSWRLRule getWSWRLRule(@NonNull String ruleName) throws WSWRLRuleException {
        if(!this.wswrlRules.containsKey(ruleName))
            throw new WSWRLRuleException("Rule " + ruleName + " does not exist.");
            
        return this.wswrlRules.get(ruleName);
    }

}