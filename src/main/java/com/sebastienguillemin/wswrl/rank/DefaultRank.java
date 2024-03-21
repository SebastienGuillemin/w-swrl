package com.sebastienguillemin.wswrl.rank;

import java.util.HashSet;
import java.util.Set;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;


public class DefaultRank implements Rank {
    private int index;

    private Set<WSWRLAtom> atoms;

    public DefaultRank(int index) {
        this.index = index;
        this.atoms = new HashSet<>();
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public Set<WSWRLAtom> getAtoms() {
        return this.atoms;
    }

    @Override
    public String toString() {
        return "Rank " + this.index + " containing " + this.atoms.size() + " atom(s).";
    }

    @Override
    public void addAtom(WSWRLAtom wswrlAtom) {
        this.atoms.add(wswrlAtom);
    }
}
