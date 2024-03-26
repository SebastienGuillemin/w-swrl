package com.sebastienguillemin.wswrl.rule.atom.property;

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
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.rule.atom.AbstractWSWRLAtom;

/**
 * {@inheritDoc}
 */
public abstract class AbstractWSWRLProperty<ObjectType extends WSWRLVariable> extends AbstractWSWRLAtom
        implements WSWRLPropertyAtom<ObjectType> {
    protected WSWRLIVariable subject;
    protected ObjectType object;
    
    protected AbstractWSWRLProperty(SWRLPredicate property, WSWRLIVariable subject, ObjectType object,
            Rank rank) {
        super(property, rank);
        this.subject = subject;
        this.object = object;
    }

    public SWRLIArgument getFirstArgument() {
        return (SWRLIArgument) this.subject;
    }

    @Override
    public WSWRLIVariable getSubject() {
        return this.subject;
    }

    @Override
    public ObjectType getObject() {
        return this.object;
    }

    @Override
    public void addSignatureEntitiesToSet(Set<OWLEntity> entities) {
        addSignatureEntitiesToSetForValue(entities, this.subject);
        addSignatureEntitiesToSetForValue(entities, this.object);
    }

    @Override
    public void addAnonymousIndividualsToSet(Set<OWLAnonymousIndividual> anons) {
        addAnonymousIndividualsToSetForValue(anons, this.subject);
        addAnonymousIndividualsToSetForValue(anons, this.object);
    }

    @Nonnull
    @Override
    public Collection<SWRLArgument> getAllArguments() {
        List<SWRLArgument> objs = new ArrayList<>();
        objs.add(this.subject);
        objs.add(this.object);
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
        diff = this.subject.compareTo(other.getFirstArgument());
        if (diff != 0) {
            return diff;
        }
        return this.object.compareTo(other.getSecondArgument());
    }
}
