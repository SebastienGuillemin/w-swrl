package com.sebastienguillemin.wswrl.core.rule.atom.binary;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;

public class DefaultWSWRLDataPropertyAtom extends AbstractWSWRLBinaryAtom<SWRLIArgument, SWRLDArgument> implements SWRLDataPropertyAtom {

    protected DefaultWSWRLDataPropertyAtom(SWRLPredicate predicate, Rank rank, float weight) {
        super(predicate, rank, weight);
    }

    protected DefaultWSWRLDataPropertyAtom(SWRLPredicate predicate, Rank rank) {
        super(predicate, rank, 1);
    }

    @Nonnull
    @Override
    public OWLDataPropertyExpression getPredicate() {
        return (OWLDataPropertyExpression) super.getPredicate();
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
        if (!(obj instanceof SWRLDataPropertyAtom)) {
            return false;
        }
        SWRLDataPropertyAtom other = (SWRLDataPropertyAtom) obj;
        return other.getPredicate().equals(getPredicate())
                && other.getFirstArgument().equals(getFirstArgument())
                && other.getSecondArgument().equals(getSecondArgument());
    }

    @Override
    public boolean isValuable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isValuable'");
    }

    @Override
    public boolean evaluate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }
    
}