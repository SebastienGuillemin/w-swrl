package com.sebastienguillemin.wswrl.core.engine;

import java.util.Hashtable;

import org.swrlapi.bridge.SWRLRuleEngineBridgeController;
import org.swrlapi.bridge.TargetSWRLRuleEngine;
import org.swrlapi.builtins.SWRLBuiltInBridgeController;
import org.swrlapi.exceptions.SWRLRuleEngineException;

import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLRuleException;

public class DefaultWSWRLRuleEngine extends DefaultSWRLRuleEngine implements WSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;
    private Hashtable<String, WSWRLRule> wswrlRules;
    private TargetWSWRLRuleEngine targetWSWRLRuleEngine;

    public DefaultWSWRLRuleEngine(
            WSWRLOntology ontology,
            TargetWSWRLRuleEngine targetWSWRLRuleEngine,
            TargetSWRLRuleEngine targetSWRLRuleEngine,
            SWRLRuleEngineBridgeController ruleEngineBridgeController,
            SWRLBuiltInBridgeController builtInBridgeController) {
        super(ontology, targetSWRLRuleEngine, ruleEngineBridgeController, builtInBridgeController);

        this.wswrlOntology = ontology;
        this.targetWSWRLRuleEngine = targetWSWRLRuleEngine;
        this.wswrlRules = new Hashtable<>();
    }

    @Override
    public WSWRLRule createWSWRLRule(String ruleName, String rule)
            throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException {
        WSWRLRule wswrlRule = this.wswrlOntology.createWSWRLRule(ruleName, rule);
        this.wswrlRules.put(ruleName, wswrlRule);
        return wswrlRule;
    }

    @Override
    public Hashtable<String, WSWRLRule> getWSWRLRules() {
        return this.wswrlRules;
    }

    @Override
    public WSWRLRule getWSWRLRule(String ruleName) throws WSWRLRuleException {
        if (!this.wswrlRules.containsKey(ruleName))
            throw new WSWRLRuleException("Rule " + ruleName + " does not exist.");

        return this.wswrlRules.get(ruleName);
    }

    @Override
    public void infer() throws SWRLRuleEngineException {
        super.infer();

        this.targetWSWRLRuleEngine.runRuleEngine();
    }
}