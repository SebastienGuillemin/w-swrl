package com.sebastienguillemin.wswrl.rule.atom.property;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
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

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLDataPropertyAtom extends AbstractWSWRLProperty<WSWRLDVariable>
        implements WSWRLDataPropertyAtom, SWRLDataPropertyAtom {

    /**
     * Constructor.
     * 
     * @param property An
     *                 {@link org.semanticweb.owlapi.model.OWLDataPropertyExpression}
     *                 represnting the property.
     * @param subject  The property subject.
     * @param object   Th eproperty object.
     * @param rank     The atom rank.
     */
    public DefaultWSWRLDataPropertyAtom(OWLDataPropertyExpression property, WSWRLIVariable subject,
            WSWRLDVariable object, Rank rank) {
        super(property, subject, object, rank);
        this.iri = property.asOWLDataProperty().getIRI();
    }

    /**
     * 
     * 
     * @param property An
     *                 {@link org.semanticweb.owlapi.model.OWLDataPropertyExpression}
     *                 represnting the property.
     * @param subject  The property subject.
     * @param object   Th eproperty object.
     */
    public DefaultWSWRLDataPropertyAtom(OWLDataPropertyExpression property, WSWRLIVariable subject,
            WSWRLDVariable object) {
        this(property, subject, object, null);
    }

    @Override
    public SWRLDArgument getSecondArgument() {
        return (SWRLDArgument) this.object;
    }

    @Nonnull
    @Override
    public OWLDataPropertyExpression getPredicate() {
        return (OWLDataPropertyExpression) super.getPredicate();
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
        if(this.getSubject().getValue() == null || this.getObject().getValue() == null)
            return false;

        return !this.getSubject().getValue().getDataProperties(this.iri).isEmpty();
    }

    @Override
    public boolean evaluate() {
        WSWRLIVariable firstVariable = this.getSubject();
        WSWRLDVariable secondVariable = this.getObject();

        for (OWLDataPropertyAssertionAxiom propertyAxiom : firstVariable.getValue().getDataProperties(this.iri)) {
            if (propertyAxiom.getObject().equals(secondVariable.getValue()))
                return true;
        }

        return false;
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }
}
