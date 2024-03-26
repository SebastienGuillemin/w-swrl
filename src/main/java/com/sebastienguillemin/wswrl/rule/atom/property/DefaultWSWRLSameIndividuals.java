package com.sebastienguillemin.wswrl.rule.atom.property;

import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLPredicate;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLSameIndividualsAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLSameIndividuals extends AbstractWSWRLProperty<WSWRLIVariable> implements WSWRLSameIndividualsAtom, SWRLSameIndividualAtom {

    public DefaultWSWRLSameIndividuals(SWRLPredicate property, WSWRLIVariable firstArgument,
            WSWRLIVariable secondArgument, Rank rank) {
        super(property, firstArgument, secondArgument, rank);
    }

    public DefaultWSWRLSameIndividuals(SWRLPredicate property, WSWRLIVariable firstArgument, WSWRLIVariable secondArgument) {
        this(property, firstArgument, secondArgument, null);
    }

    @Override
    public boolean isValuable() {
        return this.firstArgument.getValue() != null && this.secondArgument.getValue() != null;
    }

    @Override
    public boolean evaluate() {
        return this.firstArgument.getValue().equals(this.secondArgument.getValue());
    }

    @Override
    public SWRLIArgument getSecondArgument() {
        return (SWRLIArgument) this.secondArgument;
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
    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SWRLDifferentIndividualsAtom)) {
            return false;
        }
        SWRLDifferentIndividualsAtom other = (SWRLDifferentIndividualsAtom) obj;
        return other.getAllArguments().equals(getAllArguments());
    }    
}
