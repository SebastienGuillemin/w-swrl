package com.sebastienguillemin.wswrl.core.engine;

import java.util.Set;

import com.sebastienguillemin.wswrl.core.factory.TargetWSWRLRuleEngineCreator;

/**
 * This interface defines a manager to create and manage instances of WSWRL rule
 * engines. Inspired by {@link org.swrlapi.core.SWRLRuleEngineManager}
 * 
 * @see com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine
 */
public interface WSWRLRuleEngineManager {
    /**
     * Returns wheteher a rule engine is registered in the current manager or not.
     * @return True if at least one rule engine is registered.
     */
    boolean hasRegisteredRuleEngines();

    /**
     * Returns any registered rule engine name if any or null
     * @return Any registered rule engine name if any or null.
     */
    String getAnyRegisteredRuleEngineName();

    /**
     * Registers a {@link com.sebastienguillemin.wswrl.core.factory.TargetWSWRLRuleEngineCreator}.
     * @param ruleEngineCreator A creator for the rule engine.
     */
    void registerRuleEngine(TargetWSWRLRuleEngineCreator ruleEngineCreator);

    /**
     * Returns whether a rule engine is registered in the current manager or not.
     * @param ruleEngineName A rule engine name.
     * @return True if an engine with the specified name is registered.
     */
    boolean isRuleEngineRegistered(String ruleEngineName);

    /**
     * Returns all the registered rule engines names.
     * @return A list of registered rule engine names.
     */
    Set<String> getRegisteredRuleEngineNames();

    /**
     * Returns the rule engine creator for a given rule engine name.
     * @param ruleEngineName A rule engine name.
     * @return A creator for the specified rule engine; null if it is not registered.
     */
    TargetWSWRLRuleEngineCreator getRegisteredRuleEngineCreator(String ruleEngineName);

    /**
     * Unregister a SWRL rule engine from the manager.
     * @param ruleEngineName A rule engine name.
     */
    void unregisterSWRLRuleEngine(String ruleEngineName);
}
