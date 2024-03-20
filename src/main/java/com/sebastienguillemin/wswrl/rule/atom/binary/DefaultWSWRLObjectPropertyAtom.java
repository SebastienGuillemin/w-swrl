package com.sebastienguillemin.wswrl.rule.atom.binary;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.WSWRLObjectPropertyAtom;

public class DefaultWSWRLObjectPropertyAtom extends AbstractWSWRLBinaryAtom<SWRLIArgument, SWRLIArgument>
        implements WSWRLObjectPropertyAtom, SWRLObjectPropertyAtom {

    public DefaultWSWRLObjectPropertyAtom(SWRLPredicate predicate, SWRLIArgument firstArgument,
            SWRLIArgument secondArgument, Rank rank) {
        super(predicate, firstArgument, secondArgument, rank);
    }

    public DefaultWSWRLObjectPropertyAtom(SWRLPredicate predicate, SWRLIArgument firstArgument,
            SWRLIArgument secondArgument) {
        this(predicate, firstArgument, secondArgument, null);
    }

    @Nonnull
    @Override
    public OWLObjectPropertyExpression getPredicate() {
        return (OWLObjectPropertyExpression) super.getPredicate();
    }

    @Override
    public WSWRLObjectPropertyAtom getSimplifiedWSWRLObject() {
        if (getPredicate().isNamed()) {
            // named property means no inversion of arguments
            return this;
        }
        // Flip
        return new DefaultWSWRLObjectPropertyAtom(getPredicate().getInverseProperty(),
                getSecondArgument(), getFirstArgument());
    }

    @Override
    public SWRLObjectPropertyAtom getSimplified() {
        return (SWRLObjectPropertyAtom) this.getSimplifiedWSWRLObject();
    }

    @Override
    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(SWRLObjectVisitor visitor) {
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
        if (!(obj instanceof SWRLObjectPropertyAtom)) {
            return false;
        }
        SWRLObjectPropertyAtom other = (SWRLObjectPropertyAtom) obj;
        return other.getPredicate().equals(getPredicate())
                && other.getFirstArgument().equals(getFirstArgument())
                && other.getSecondArgument().equals(getSecondArgument());
    }

    @Override
    public boolean isValuable() {
        return true;
    }

    @Override
    public boolean evaluate() {
        return true;
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }
}
