package com.sebastienguillemin.wswrl.core.rule.atom;

import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.WSWRLAtom;

import lombok.Getter;
import lombok.Setter;
import uk.ac.manchester.cs.owl.owlapi.SWRLAtomImpl;

public abstract class AbstractWSWRLAtom extends SWRLAtomImpl implements WSWRLAtom {
    @Getter
    @Setter
    protected float weight;

    @Getter
    @Setter
    private Rank rank;

    protected AbstractWSWRLAtom(SWRLPredicate predicate, Rank rank, float weight) {
        super(predicate);

        this.rank = rank;
        this.weight = weight;
    }

    protected AbstractWSWRLAtom(SWRLPredicate predicate, Rank rank) {
        this(predicate, rank, 1);
    }
}
