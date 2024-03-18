package com.sebastienguillemin.wswrl.core;

import java.util.Hashtable;
import java.util.Optional;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLRuleEngineException;
import org.swrlapi.exceptions.SWRLRuleException;
import org.swrlapi.parser.SWRLParseException;

import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLRuleEngineException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLRuleException;

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
    void infer() throws WSWRLRuleEngineException;

    /**
     * Create a SWRL rule
     *
     * @param ruleName The name of the rule
     * @param rule     The rule text
     * @return A SWRL rule
     * @throws SWRLParseException If an error occurs during parsing
     */
    WSWRLRule createWSWRLRule(String ruleName, String rule)
            throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException;

    /**
     * Create a SWRL rule and associate a comment and active state with it.
     *
     * @param ruleName The name of the rule
     * @param rule     The rule text
     * @param comment  A comment associated with the rule
     * @param isActive Is the rule active
     * @return A SWRL rule
     * @throws SWRLParseException If an error occurs during parsing
     */
    WSWRLRule createSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException;

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
