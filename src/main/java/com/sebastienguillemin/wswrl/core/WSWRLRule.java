package com.sebastienguillemin.wswrl.core;

import java.util.List;
import java.util.Set;

/**
 * A WSWRL rule.
 * 
 * @see WSWRLAtom
 * 
 */
public interface WSWRLRule {
    /**
     * @return The name of the rule.
     */
    public String getRuleName();

    /**
     * @return A comment annotation associated with a rule.
     */
    public String getComment();

    /**
     * @return True if the rule is enables.
     */
    public boolean isEnabled();

    /**
     * @return The body of the current rule.
     */
    public Set<WSWRLAtom> getBody();

    /**
     * @return The head of the current rule.
     */    
    public Set<WSWRLAtom> getHead();

    /**
     * This method is used to calculate
     * {@link com.sebastienguillemin.wswrl.core.WSWRLAtom} weights.
     */
    public void computeWeights();

    /**
     * @return The head confidence.
     */
    public float getConfidence();

    /**
     * Returns the atoms of a specific rank of the rule.
     * 
     * @param rankIndex The rank index of the atoms.
     * @return The set of {@link com.sebastienguillemin.wswrl.core.WSWRLAtom} at a
     *         certain rank.
     */
    public Set<WSWRLAtom> atRank(int rankIndex);

    /**
     * Return the set of valuable atoms in a given set of atoms.
     * 
     * @param atoms a set of {@link com.sebastienguillemin.wswrl.core.WSWRLAtom} to
     *              test
     * @return the set of valuable atoms.
     */
    public Set<WSWRLAtom> valuable(Set<WSWRLAtom> atoms);
}
