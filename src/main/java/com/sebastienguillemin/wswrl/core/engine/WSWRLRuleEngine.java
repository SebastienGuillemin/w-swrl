package com.sebastienguillemin.wswrl.core.engine;

import java.util.Hashtable;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLRuleEngineException;

import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.exception.WSWRLRuleEngineException;
import com.sebastienguillemin.wswrl.exception.WSWRLRuleException;

/**
 * This interface provides must that must be implemented be a WSWRL Rule engine.
 * It is inspired from {@link org.swrlapi.core.SWRLRuleEngine}.
 * Some methods would be added in the futur.
 */
public interface WSWRLRuleEngine extends SWRLRuleEngine {
    /**
     * Load rules and knowledge from OWL, send them to the rule engine, run the rule
     * engine, and write any inferred
     * knowledge back to OWL.
     *
     * @throws SWRLRuleEngineException If an error occurs during inference
     */
    @Override
    void infer() throws WSWRLRuleEngineException;

    /**
     * Create a SWRL rule
     *
     * @param ruleName The name of the rule
     * @param rule     The rule text
     * @return A SWRL rule
     * @throws WSWRLParseException @see com.sebastienguillemin.wswrl.exception.WSWRLParseException
     * @throws WSWRLBuiltInException @see com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException
     * @throws MissingRankException @see com.sebastienguillemin.wswrl.exception.MissingRankException
     */
    WSWRLRule createWSWRLRule(String ruleName, String rule)
            throws WSWRLParseException, WSWRLBuiltInException, MissingRankException;

    /**
     * @return A collection of WSWRL rules
     * @throws WSWRLRuleException If an error occurs
     */
    @NonNull
    Hashtable<String, WSWRLRule> getWSWRLRules() throws WSWRLRuleException;

    /**
     * @param ruleName The name of the rule
     * @return A WSWRL rule
     * @throws WSWRLRuleException If an error occurs
     */
    WSWRLRule getWSWRLRule(@NonNull String ruleName) throws WSWRLRuleException;
}
