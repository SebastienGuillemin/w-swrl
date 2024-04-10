package com.sebastienguillemin.wswrl.core.ontology;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.swrlapi.core.SWRLAPIOWLOntology;

import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.rule.WSWRLAxiom;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException;
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
    WSWRLRule createWSWRLRule(String ruleName, String rule)
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
    WSWRLRule createWSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, MissingRankException;

    /**
     * Returns all the ontology WSWRL rules.
     * 
     * @return All the WSWRL rules created for the current ontology.
     */
    Set<WSWRLRule> getWSWRLRules();

    /**
     * Returns the ontology's data factory.
     * 
     * @return A {@link com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory}
     *         instance.
     */
    WSWRLDataFactory getWSWRLDataFactory();

    /**
     * Add an axiom inferred by a WSWRL rule. All the inferred axioms are stored
     * in a cache. This cache can be clear by calling
     * {@link #clearInferredAxiomsCache()}. The cached axioms can be added
     * permanently to the ontology by calling the WSWRL ontology manager method :
     * {@link com.sebastienguillemin.wswrl.core.ontology.WSWRLOntologyManager#writeInferredAxiomsToOntology(WSWRLOntology)}.
     * 
     * @param atom       A set of Atoms to add to the ontology.
     * @param confidence The confidence associated to the new atoms.
     * 
     * @see com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine
     * @see com.sebastienguillemin.wswrl.core.rule.WSWRLRuleResult
     */
    void addWSWRLInferredAxiom(Set<WSWRLAtom> atoms, float confidence);

    /**
     * Returns the inferred axioms by WSWRL rules as OWL axiom.
     * 
     * @return A set of OWL axiom.
     */
    Set<OWLAxiom> getWSWRLInferredAxiomsAsOWLAxiom();

    /**
     * Returns the inferred axioms by WSWRL rules.
     * 
     * @return A set of OWL axiom.
     */
    Set<WSWRLAxiom> getWSWRLInferredAxioms();

    /**
     * Clear the inferred axiom cache.
     */
    void clearInferredAxiomsCache();

    /**
     * Process the ontology axioms. Must be called once the ontology is created to
     * retrieve OWLAxioms from the new ontology.
     */
    @Override
    void processOntology() throws WSWRLBuiltInException;

    /**
     * Remove all inferred axioms whose confidence is less than {@code threshold}.
     * 
     * @param threshold The minimum required confidence to keep an inferred axiom.
     */
    void filterInferredAxioms(float threshold);

    /**
     * Return the base IRI of the ontology.
     * 
     * @return The ontology's base IRI
     */
    IRI getBaseIRI();
}
