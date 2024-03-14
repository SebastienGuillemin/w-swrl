package com.sebastienguillemin.wswrl.core.rule.atom.binary;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLPredicate;
import org.semanticweb.owlapi.util.OWLObjectTypeIndexProvider;

import com.sebastienguillemin.wswrl.core.Rank;

import uk.ac.manchester.cs.owl.owlapi.SWRLObjectPropertyAtomImpl;

public class DefaultWSWRLObjectPropertyAtom extends AbstractWSWRLBinaryAtom<SWRLIArgument, SWRLIArgument> implements SWRLObjectPropertyAtom {

    protected DefaultWSWRLObjectPropertyAtom(SWRLPredicate predicate, Rank rank, float weight) {
        super(predicate, rank, weight);
    }

    protected DefaultWSWRLObjectPropertyAtom(SWRLPredicate predicate, Rank rank) {
        super(predicate, rank, 1);
    }

    @Override
    protected int index() {
        return OWLObjectTypeIndexProvider.RULE_OBJECT_TYPE_INDEX_BASE + 3;
    }

    @Nonnull
    @Override
    public OWLObjectPropertyExpression getPredicate() {
        return (OWLObjectPropertyExpression) super.getPredicate();
    }

    @Override
    public SWRLObjectPropertyAtom getSimplified() {
        if (getPredicate().isNamed()) {
            // named property means no inversion of arguments
            return this;
        }
        // Flip
        return new SWRLObjectPropertyAtomImpl(getPredicate().getInverseProperty(),
            getSecondArgument(), getFirstArgument());
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isValuable'");
    }

    @Override
    public boolean evaluate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }
}
