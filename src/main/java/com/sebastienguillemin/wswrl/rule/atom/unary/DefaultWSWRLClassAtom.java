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
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLClassAtom extends AbstractWSWRLUnaryAtom<WSWRLIVariable>
        implements WSWRLClassAtom, SWRLClassAtom {

    /**
     * Constructor.
     * 
     * @param classExpression An {@link org.semanticweb.owlapi.model.OWLClassExpression} representing the class atom.
     * @param argument  The atom argument.
     * @param rank     The atom rank.
     */
    public DefaultWSWRLClassAtom(OWLClassExpression classExpression, WSWRLIVariable argument, Rank rank) {
        super((SWRLPredicate) classExpression, argument, rank);
        this.iri = classExpression.asOWLClass().getIRI();
    }

    /**
     * Constructor without rank (set to {@code null}).
     * 
     * @param classExpression An {@link org.semanticweb.owlapi.model.OWLClassExpression} representing the class atom.
     * @param argument  The atom argument.
     */
    public DefaultWSWRLClassAtom(OWLClassExpression classExpression, WSWRLIVariable argument) {
        this(classExpression, argument, null);
    }

    @Override
    public SWRLIArgument getArgument() {
        return (SWRLIArgument) this.getWSWRLArgument();
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
        return this.getWSWRLArgument().getValue().getOWLClass(this.iri) != null;
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }
}
