package com.sebastienguillemin.wswrl.rule.atom;

import java.util.Hashtable;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIArgument;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;

public class DefaultWSWRLObjectPropertyAtom extends AbstractWSWRLProperty<WSWRLIArgument> implements WSWRLObjectPropertyAtom, SWRLObjectPropertyAtom {

    public DefaultWSWRLObjectPropertyAtom(OWLObjectPropertyExpression property, WSWRLIArgument firstArgument, WSWRLIArgument secondArgument, Rank rank) {
        super(property, firstArgument, secondArgument, rank);
        this.iri = property.asOWLObjectProperty().getIRI();

    }

    public DefaultWSWRLObjectPropertyAtom(OWLObjectPropertyExpression property, WSWRLIArgument firstArgument, WSWRLIArgument secondArgument) {
        this(property, firstArgument, secondArgument, null);
    }

    public SWRLIArgument getSecondArgument() {
        return (SWRLIArgument) this.secondArgument;
    }

    @Nonnull
    @Override
    public OWLObjectPropertyExpression getPredicate() {
        return (OWLObjectPropertyExpression) super.getPredicate();
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
        // WSWRLVariable firstVariable = (WSWRLVariable) this.getFirstArgument();
        // WSWRLVariable secondVariable = (WSWRLVariable) this.getSecondArgument();

        return false;
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }

    @Override
    public void parseObject(Hashtable<IRI, WSWRLIndividual> individuals) {
        // this.object = this.parseIndividual((OWLNamedIndividual) this.propertyAssertion.getObject(), individuals);
    }

    @Override
    public SWRLObjectPropertyAtom getSimplified() {
        return (SWRLObjectPropertyAtom) this;
    }

    @Override
    public void addPropertyToIndividual(WSWRLIndividual individual) {
        individual.addObjectProperty(this);
    }
}
