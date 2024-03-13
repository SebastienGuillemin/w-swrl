package com.sebastienguillemin.wswrl.core.rule.atom;

import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.WSWRLHeadAtom;

public abstract class AbstractWSWRLHeadAtom extends AbstractWSWRLAtom implements WSWRLHeadAtom {

    protected AbstractWSWRLHeadAtom(SWRLPredicate predicate, Rank rank) {
        super(predicate, rank, 1);
    }

    public void setConfidence(float confidence) {
        this.weight = confidence;
    }

    public float getConfidence() {
        return this.weight;
    }

}
