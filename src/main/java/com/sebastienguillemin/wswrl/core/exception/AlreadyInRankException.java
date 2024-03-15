package com.sebastienguillemin.wswrl.core.exception;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.WSWRLAtom;

public class AlreadyInRankException extends Exception {
    private Rank rank;
    private WSWRLAtom atom;

    public AlreadyInRankException(WSWRLAtom atom, Rank rank) {
        this.rank = rank;
        this.atom = atom;
    }

    @Override
    public String getMessage() {
        return "Rank " + this.rank.getIndex() + " already contains atom " + this.atom.getPredicate();
    }
}