package com.sebastienguillemin.wswrl.core;

import java.util.List;

/**
 * A {@link com.sebastienguillemin.wswrl.core.WSWRLAtom} rank.
 * Each rank is identified by an index (an {@code int}).
 */
public interface Rank {
    /**
     * 
     * @return The rank index.
     */
    public int getIndex();

    /**
     * Return the list of atoms assigned to the current rank.
     * @return The list of atoms assigned to the rank.
     */
    public List<WSWRLAtom> getAtoms();
}