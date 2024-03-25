package com.sebastienguillemin.wswrl.rule.atom.property;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;

public class DefaultWSWRLDataPropertyAtom extends AbstractWSWRLProperty<WSWRLDVariable> implements WSWRLDataPropertyAtom, SWRLDataPropertyAtom {

    public DefaultWSWRLDataPropertyAtom(OWLDataPropertyExpression property, WSWRLIVariable firstArgument, WSWRLDVariable secondArgument, Rank rank) {
        super(property, firstArgument, secondArgument, rank);
        this.iri = property.asOWLDataProperty().getIRI();
    }

    public DefaultWSWRLDataPropertyAtom(OWLDataPropertyExpression property, WSWRLIVariable firstArgument, WSWRLDVariable secondArgument) {
        this(property, firstArgument, secondArgument, null);
    }

    public SWRLDArgument getSecondArgument() {
        return (SWRLDArgument) this.secondArgument;
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
        return this.evaluate();
    }

    @Override
    public boolean evaluate() {
        return this.getSecondWSWRLArgument().getValue() != null;
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }
}
