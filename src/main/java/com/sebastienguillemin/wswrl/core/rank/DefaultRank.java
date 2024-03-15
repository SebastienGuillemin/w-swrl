package com.sebastienguillemin.wswrl.core.rank;

import java.util.ArrayList;
import java.util.List;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;

public class DefaultRank implements Rank {
    private int index;

    private List<WSWRLAtom> atoms;

    public DefaultRank(int index) {
        this.index = index;
        this.atoms = new ArrayList<>();
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public List<WSWRLAtom> getAtoms() {
        return this.atoms;
    }

    @Override
    public String toString() {
        return "Rank " + this.index + " containing " + this.atoms.size() + " atom(s).";
    }

    @Override
    public void addAtom(WSWRLAtom wswrlAtom) throws AlreadyInRankException {

        for (WSWRLAtom atom : this.atoms) {
            if (wswrlAtom.equals(atom))
                throw new AlreadyInRankException(wswrlAtom, this);
        }

        this.atoms.add(wswrlAtom);
    }
}
