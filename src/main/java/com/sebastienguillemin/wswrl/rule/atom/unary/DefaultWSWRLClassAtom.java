package com.sebastienguillemin.wswrl.rule.atom.unary;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.WSWRLClassAtom;

public class DefaultWSWRLClassAtom extends AbstractWSWRLUnaryAtom<SWRLIArgument> implements WSWRLClassAtom, SWRLClassAtom {

    public DefaultWSWRLClassAtom(SWRLPredicate predicate, SWRLIArgument argument, Rank rank) {
        super(predicate, argument, rank);
    }
    
    @Nonnull
    @Override
    public OWLClassExpression getPredicate() {
        return (OWLClassExpression) super.getPredicate();
    }

    @Override
    public void accept(SWRLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(SWRLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SWRLClassAtom)) {
            return false;
        }
        SWRLClassAtom other = (SWRLClassAtom) obj;
        return other.getArgument().equals(getArgument())
                && other.getPredicate().equals(getPredicate());
    }

    @Override
    protected int compareObjectOfSameType(OWLObject object) {
        SWRLClassAtom other = (SWRLClassAtom) object;
        int diff = getPredicate().compareTo(other.getPredicate());
        if (diff != 0) {
            return diff;
        }
        return getArgument().compareTo(other.getArgument());
    }

    @Override
    public boolean isValuable() {
        return true;
    }

    @Override
    public boolean evaluate() {
        return false;
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }
}
