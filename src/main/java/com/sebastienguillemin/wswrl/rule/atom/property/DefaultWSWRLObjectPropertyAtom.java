package com.sebastienguillemin.wswrl.rule.atom.property;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLObjectPropertyAtom extends AbstractWSWRLProperty<WSWRLIVariable>
        implements WSWRLObjectPropertyAtom, SWRLObjectPropertyAtom {

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
    public DefaultWSWRLObjectPropertyAtom(OWLObjectPropertyExpression property, WSWRLIVariable subject,
            WSWRLIVariable object, Rank rank) {
        super(property, subject, object, rank);
        this.iri = property.asOWLObjectProperty().getIRI();

    }

    /**
     * Constructor without rank (set to {@code null}).
     * 
     * @param property An
     *                 {@link org.semanticweb.owlapi.model.OWLDataPropertyExpression}
     *                 represnting the property.
     * @param subject  The property subject.
     * @param object   Th eproperty object.
     */
    public DefaultWSWRLObjectPropertyAtom(OWLObjectPropertyExpression property, WSWRLIVariable subject,
            WSWRLIVariable object) {
        this(property, subject, object, null);
    }

    public SWRLIArgument getSecondArgument() {
        return (SWRLIArgument) this.object;
    }

    @Nonnull
    @Override
    public OWLObjectPropertyExpression getPredicate() {
        return (OWLObjectPropertyExpression) super.getPredicate();
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
        if(this.getSubject().getValue() == null || this.getObject().getValue() == null)
            return false;

        boolean res = !this.getSubject().getValue().getObjectProperties(this.iri).isEmpty();
        return res;

    }

    @Override
    public boolean evaluate() {
        WSWRLIVariable firstVariable = this.getSubject();
        WSWRLIVariable secondVariable = this.getObject();

        for (OWLObjectPropertyAssertionAxiom propertyAxiom : firstVariable.getValue().getObjectProperties(this.iri)) {
            if (propertyAxiom.getObject().asOWLNamedIndividual().getIRI().equals(secondVariable.getValue().getIRI()))
                return true;
        }

        return false;
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }

    @Override
    public SWRLObjectPropertyAtom getSimplified() {
        return (SWRLObjectPropertyAtom) this;
    }
}
