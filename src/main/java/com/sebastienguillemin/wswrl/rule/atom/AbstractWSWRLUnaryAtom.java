package com.sebastienguillemin.wswrl.rule.atom;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLPredicate;
import org.semanticweb.owlapi.util.CollectionFactory;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLUnaryAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;

public abstract class AbstractWSWRLUnaryAtom<A extends WSWRLVariable> extends AbstractWSWRLAtom implements WSWRLUnaryAtom<A> {
    protected A argument;

    protected AbstractWSWRLUnaryAtom(SWRLPredicate predicate, A argument, Rank rank) {
        super(predicate, rank);
        this.argument = argument;
    }

    @Override
    public A getWSWRLArgument() {
        return this.argument;
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
