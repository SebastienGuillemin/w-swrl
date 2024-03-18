package com.sebastienguillemin.wswrl.core;

import java.util.Set;

public interface WSWRLRuleEngineManager {
    /**
     * @return True if at least one rule engine is registered
     */
    boolean hasRegisteredRuleEngines();

    /**
     * @return Any registered rule engine name - if any.
     */
    String getAnyRegisteredRuleEngineName();

    /**
     * @param ruleEngineCreator A creator for the rule engine
     */
    void registerRuleEngine(TargetWSWRLRuleEngineCreator ruleEngineCreator);

    /**
     * @param ruleEngineName A rule engine name
     * @return True if an engine with the specified name is registered
     */
    boolean isRuleEngineRegistered(String ruleEngineName);

    /**
     * @return A list of registered rule engine names
     */
    Set<String> getRegisteredRuleEngineNames();

    /**
     * @param ruleEngineName A rule engine name
     * @return A creator for the specified rule engine; null if it is not registered
     */
    TargetWSWRLRuleEngineCreator getRegisteredRuleEngineCreator(String ruleEngineName);

    /**
     * @param ruleEngineName A rule engine name
     */
    void unregisterSWRLRuleEngine(String ruleEngineName);
}
