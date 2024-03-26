package com.sebastienguillemin.wswrl.core.ontology;

import java.util.Set;

import org.swrlapi.core.SWRLAPIOWLOntology;

import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLParseException;

/**
 * Extension of the {@link org.swrlapi.core.SWRLAPIOWLOntology} adapted for the
 * WSWRL rules.
 */
public interface WSWRLOntology extends SWRLAPIOWLOntology {

    /**
     * Create a WSWRL in the current ontology.
     * 
     * @see com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology#createSQWRLQuery(String,
     *      String, String, boolean)
     * 
     * @param ruleName The rule name.
     * @param rule     The string representation if the rule.
     * @return A new {@link com.sebastienguillemin.wswrl.core.rule.WSWRLRule}
     *         created from the {@code rule} parameter. The WSWRL rule name is the
     *         {@code ruleName} parameter value.
     * 
     * @throws WSWRLParseException  If an error occurs when parsong the rule.
     * @throws MissingRankException When a rank is not used but an higher rank is.
     */
    public WSWRLRule createWSWRLRule(String ruleName, String rule)
            throws WSWRLParseException, MissingRankException;

    /**
     * Create a WSWRL rule in the current ontology.
     * 
     * @param ruleName The rule name.
     * @param rule     The string representation if the rule.
     * @param comment  The rule comment.
     * @param isActive True is the rule is active, false otherwise.
     * 
     * @return A new {@link com.sebastienguillemin.wswrl.core.rule.WSWRLRule}
     *         created from the {@code rule} parameter. The WSWRL rule name, comment
     *         and state (active or not) correspond respectively to the values of
     *         the parameters {@code ruleName}, {@code comment} and
     *         {@code isActive}.
     * 
     * @throws WSWRLParseException
     * @throws MissingRankException
     */
    public WSWRLRule createWSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, MissingRankException;

    /**
     * Returns all the ontology WSWRL rules.
     * @return All the WSWRL rules created for the current ontology.
     */
    public Set<WSWRLRule> getWSWRLRules();

    /**
     * Returns the ontology's data factory.
     * @return A {@link com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory} instance.
     */
    public WSWRLDataFactory getWSWRLDataFactory();
}
