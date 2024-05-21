package com.sebastienguillemin.wswrl.core.rule;

import java.util.Set;

import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.exception.WeightCalculationException;

/**
 * A WSWRL rule.
 * 
 * @see WSWRLAtom
 * 
 */
public interface WSWRLRule {
    /**
     * Returns the rule name.
     * 
     * @return The name of the rule.
     */
    public String getRuleName();

    /**
     * Returns the rule comment.
     * 
     * @return A comment annotation associated with a rule.
     */
    public String getComment();

    /**
     * Returns whether the rule is enable or not.
     * 
     * @return True if the rule is enables.
     */
    public boolean isEnabled();

    /**
     * Returns the WSWRL atoms composing the rule head.
     * 
     * @return The head of the current rule.
     */
    public Set<WSWRLAtom> getHead();

    /**
     * Returns the WSWRL atoms composing the rule body.
     * 
     * @return The body of the current rule.
     */
    public Set<WSWRLAtom> getBody();

    /**
     * Calculates the rule WSWRL atom weights.
     * 
     * @see com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom
     */
    public void calculateWeights() throws WeightCalculationException;

    /**
     * Calculates the rule confidence. Must be called after binding the variables
     * and
     * calling the calculateWeights function.
     */
    public float calculateConfidence();

    /**
     * Returns the atoms of a specific rank of the rule.
     * 
     * @param rankIndex The rank index of the atoms.
     * @return The set of
     *         {@link com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom} at a
     *         certain rank.
     */
    public Set<WSWRLAtom> atRank(int rankIndex);

    /**
     * Return the set of valuable atoms in a given set of atoms.
     * 
     * @param atoms a set of
     *              {@link com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom} to
     *              test
     * @return the set of valuable atoms.
     */
    public Set<WSWRLAtom> valuable(Set<WSWRLAtom> atoms);

    /**
     * Returns the cause of a skip. A skip occurs when a rank 0 atom is not valuable
     * or evaluated to {@code false}. In this case, this atom will by used by
     * {@link com.sebastienguillemin.wswrl.core.rule.variable.binding.VariableBinding#skipByCause(WSWRLAtom)}
     * to ignore some useless binding.
     * 
     * @return A rank 0atom which is not valuable or evaluated to {@code false}.
     */
    public WSWRLAtom getAtomCausedSkip();
}
