package com.sebastienguillemin.wswrl.core;

import org.semanticweb.owlapi.model.SWRLAtom;

/**
 * A {@link com.sebastienguillemin.wswrl.core.WSWRLRule} atom associated with a weight ranging in [0; 1].
 * 
 * @see org.semanticweb.owlapi.model.SWRLAtom
 * @see com.sebastienguillemin.wswrl.core.WSWRLHeadAtom
 */
public interface WSWRLAtom extends SWRLAtom {
    /**
     * 
     * @return The atom weight.
     */
    public float getWeight();

    /**
     * Set the rank of the current atom.
     * 
     * @param rank The atom rank.
     */
    public void setRank(Rank rank);

    /**
     * 
     * @return The atom rank.
     */
    public Rank getRank();

    /**
     * Return weither the current atom is valuable.
     * 
     * @return {@code True} if the atom is valuable {@code False} otherwise.
     */
    public boolean isValuable();

    /**
     * Returns the true value (i.e., {@code True} or {@code False}) of the current
     * atom for a certain valuation.
     * 
     * @return The atom truth value.
     */
    public boolean evaluate();
}
