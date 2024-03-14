package com.sebastienguillemin.wswrl.core.rule.atom.binary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLBinaryAtom;
import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.AbstractWSWRLAtom;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractWSWRLBinaryAtom<A extends SWRLArgument, B extends SWRLArgument> extends AbstractWSWRLAtom implements SWRLBinaryAtom<A, B> {
    @Getter
    @Setter
    protected A firstArgument;

    @Getter
    @Setter
    protected B secondArgument;

    protected AbstractWSWRLBinaryAtom(SWRLPredicate predicate, Rank rank, float weight) {
        super(predicate, rank, weight);
    }

    protected AbstractWSWRLBinaryAtom(SWRLPredicate predicate, Rank rank) {
        this(predicate, rank, 1);
    }
    
    @Override
    public void addSignatureEntitiesToSet(Set<OWLEntity> entities) {
        addSignatureEntitiesToSetForValue(entities, this.firstArgument);
        addSignatureEntitiesToSetForValue(entities, this.secondArgument);
    }

    @Override
    public void addAnonymousIndividualsToSet(Set<OWLAnonymousIndividual> anons) {
        addAnonymousIndividualsToSetForValue(anons, this.firstArgument);
        addAnonymousIndividualsToSetForValue(anons, this.secondArgument);
    }

    @Nonnull
    @Override
    public Collection<SWRLArgument> getAllArguments() {
        List<SWRLArgument> objs = new ArrayList<>();
        objs.add(this.firstArgument);
        objs.add(this.secondArgument);
        return objs;
    }

    @Override
    protected int compareObjectOfSameType(OWLObject object) {
        SWRLBinaryAtom<?, ?> other = (SWRLBinaryAtom<?, ?>) object;
        int diff = ((OWLObject) getPredicate()).compareTo((OWLObject) other
                .getPredicate());
        if (diff != 0) {
            return diff;
        }
        diff = this.firstArgument.compareTo(other.getFirstArgument());
        if (diff != 0) {
            return diff;
        }
        return this.secondArgument.compareTo(other.getSecondArgument());
    }
}
