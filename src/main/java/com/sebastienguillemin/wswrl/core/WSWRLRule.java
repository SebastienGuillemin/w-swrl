package com.sebastienguillemin.wswrl.core;

import java.util.List;

/**
 * A WSWRL rule.
 * 
 * @see WSWRLAtom
 * 
 */
public interface WSWRLRule {
    /**
     * @return The name of the rule
     */
    public String getRuleName();

    /**
     * @return A comment annotation associated with a rule
     */
    public String getComment();

    /**
     * @return True if the rule is active
     */
    public boolean isActive();

    /**
     *
     * @param active The active state
     */
    public void setActive(boolean active);

    /**
     * @return A list of SWRL atoms
     */
    public List<WSWRLAtom> getBodyAtoms();

    /**
     * @return The {@link com.sebastienguillemin.wswrl.core.WSWRLHeadAtom} of the
     *         current rule.
     */    
    public WSWRLAtom getHeadAtom();

    /**
     * This method is used to calculate
     * {@link com.sebastienguillemin.wswrl.core.WSWRLAtom} weights.
     */
    public void computeWeights();

    /**
     * Returns the atoms of a specific rank of the rule.
     * 
     * @param rankIndex The rank index of the atoms.
     * @return The list of {@link com.sebastienguillemin.wswrl.core.WSWRLAtom} at a
     *         certain rank.
     */
    public List<WSWRLAtom> atRank(int rankIndex);

    /**
     * Return the liste of valuable atoms in a given list of atoms.
     * 
     * @param atoms a list of {@link com.sebastienguillemin.wswrl.core.WSWRLAtom} to
     *              test
     * @return the list of valuable atoms.
     */
    public List<WSWRLAtom> valuable(List<WSWRLAtom> atoms);
}
