package com.sebastienguillemin.wswrl.core;

import java.util.List;

import org.swrlapi.core.SWRLAPIRule;

/**
 * A WSWRL rule.
 * 
 * @see WSWRLAtom
 * 
 */
public interface WSWRLRule extends SWRLAPIRule {
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

    /**
     * @return The {@link com.sebastienguillemin.wswrl.core.WSWRLHeadAtom} of the
     *         current rule.
     */
    public WSWRLHeadAtom getHeadAtom();
}
