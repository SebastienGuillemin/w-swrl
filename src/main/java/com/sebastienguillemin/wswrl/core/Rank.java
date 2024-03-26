package com.sebastienguillemin.wswrl.core;

import java.util.Set;

import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;

/**
 * A {@link com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom} rank.
 * Each rank is identified by an index (an {@code int}).
 */
public interface Rank {
    /**
     * Returns the rank index.
     * @return The rank index.
     */
    public int getIndex();

    /**
     * Return the set of atoms assigned to the current rank.
     * 
     * @return The set of atoms assigned to the rank.
     */
    public Set<WSWRLAtom> getAtoms();

    /**
     * Add an atom the current rank.
     * @param atom The atom to add.
     */
    public void addAtom(WSWRLAtom atom);
}
