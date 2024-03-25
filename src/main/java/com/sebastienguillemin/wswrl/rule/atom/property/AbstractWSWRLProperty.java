package com.sebastienguillemin.wswrl.rule.atom.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLBinaryAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.rule.atom.AbstractWSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;

public abstract class AbstractWSWRLProperty<ObjectType extends WSWRLVariable> extends AbstractWSWRLAtom implements WSWRLPropertyAtom<ObjectType> {
    protected WSWRLIVariable firstArgument;
    protected ObjectType secondArgument;

    protected AbstractWSWRLProperty(OWLPropertyExpression property, WSWRLIVariable firstArgument, ObjectType secondArgument, Rank rank) {
        super((SWRLPredicate) property, rank);
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
    }


    public SWRLIArgument getFirstArgument() {
        return (SWRLIArgument) this.firstArgument;
    }

    public WSWRLIVariable getFirstWSWRLArgument() {
        return this.firstArgument;
    }    

    public ObjectType getSecondWSWRLArgument() {
        return this.secondArgument;
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
