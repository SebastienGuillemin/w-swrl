package com.sebastienguillemin.wswrl.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngineCreator;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngineManager;

public class DefaultWSWRLRuleEngineManager implements WSWRLRuleEngineManager {
    private Map<String, TargetWSWRLRuleEngineCreator> registeredSWRLRuleEngines;

    public DefaultWSWRLRuleEngineManager() {
        this.registeredSWRLRuleEngines = new HashMap<>();
    }

    @Override
    public void registerRuleEngine(TargetWSWRLRuleEngineCreator ruleEngineCreator) {
        String ruleEngineName = ruleEngineCreator.getRuleEngineName();

        if (this.registeredSWRLRuleEngines.containsKey(ruleEngineName)) {
            this.registeredSWRLRuleEngines.remove(ruleEngineName);
        }
        this.registeredSWRLRuleEngines.put(ruleEngineName, ruleEngineCreator);
    }

    @Override
    public boolean isRuleEngineRegistered(String ruleEngineName) {
        return this.registeredSWRLRuleEngines.containsKey(ruleEngineName);
    }

    @Override
    public boolean hasRegisteredRuleEngines() {
        return !this.registeredSWRLRuleEngines.isEmpty();
    }

    @Override
    public String getAnyRegisteredRuleEngineName() {
        if (hasRegisteredRuleEngines())
            return this.registeredSWRLRuleEngines.keySet().iterator().next();
        else
            return null;
    }

    @Override
    public TargetWSWRLRuleEngineCreator getRegisteredRuleEngineCreator(
            String ruleEngineName) {
        if (this.registeredSWRLRuleEngines.containsKey(ruleEngineName))
            return this.registeredSWRLRuleEngines.get(ruleEngineName);
        else
            return null;
    }

    
    @Override
    public Set<String> getRegisteredRuleEngineNames() {
        return this.registeredSWRLRuleEngines.keySet();
    }

    @Override
    public void unregisterSWRLRuleEngine(String ruleEngineName) {
        if (this.registeredSWRLRuleEngines.containsKey(ruleEngineName))
            this.registeredSWRLRuleEngines.remove(ruleEngineName);
    }
}
