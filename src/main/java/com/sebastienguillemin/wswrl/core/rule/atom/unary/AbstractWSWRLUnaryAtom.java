package com.sebastienguillemin.wswrl.core.rule.atom.unary;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLPredicate;
import org.semanticweb.owlapi.model.SWRLUnaryAtom;
import org.semanticweb.owlapi.util.CollectionFactory;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.AbstractWSWRLAtom;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractWSWRLUnaryAtom<A extends SWRLArgument> extends AbstractWSWRLAtom implements SWRLUnaryAtom<A> {
    @Getter
    @Setter
    protected A argument;

    protected AbstractWSWRLUnaryAtom(SWRLPredicate predicate, A argument, Rank rank, float weight) {
        super(predicate, rank, weight);
        this.argument = argument;
    }

    protected AbstractWSWRLUnaryAtom(SWRLPredicate predicate, A argument, Rank rank) {
        super(predicate, rank);
    }

    @Override
    public void addSignatureEntitiesToSet(Set<OWLEntity> entities) {
        addSignatureEntitiesToSetForValue(entities, this.argument);
    }

    @Override
    public void addAnonymousIndividualsToSet(Set<OWLAnonymousIndividual> anons) {
        addAnonymousIndividualsToSetForValue(anons, this.argument);
    }

    @Nonnull
    @Override
    public Collection<SWRLArgument> getAllArguments() {
        return CollectionFactory.createSet((SWRLArgument) this.argument);
    }
}
