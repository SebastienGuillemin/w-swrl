package com.sebastienguillemin.wswrl.core.rule.atom;

import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;

import lombok.Getter;
import lombok.Setter;
import uk.ac.manchester.cs.owl.owlapi.SWRLAtomImpl;

public abstract class AbstractWSWRLAtom extends SWRLAtomImpl implements WSWRLAtom {
    @Getter
    @Setter
    protected float weight;

    @Getter
    private Rank rank;

    public void setRank(Rank rank) throws AlreadyInRankException {
        this.rank = rank;
        rank.addAtom(this);
        System.out.println("Setting rank : " + rank);
    }

    protected AbstractWSWRLAtom(SWRLPredicate predicate, Rank rank) {
        super(predicate);
        this.rank = rank;
    }
}
