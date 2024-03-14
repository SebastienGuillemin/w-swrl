package com.sebastienguillemin.wswrl.core;

import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.exceptions.SWRLRuleEngineException;
import org.swrlapi.parser.SWRLParseException;

import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLRuleEngineException;

public interface WSWRLRuleEngine {
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
    WSWRLRule createWSWRLRule(String ruleName, String rule) throws WSWRLParseException, WSWRLBuiltInException;

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
            throws SWRLParseException, SWRLBuiltInException;
}
