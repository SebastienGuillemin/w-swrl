package com.sebastienguillemin.wswrl.rule.atom.unary;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataRangeAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLDataRangeAtom extends AbstractWSWRLUnaryAtom<WSWRLDVariable> implements WSWRLDataRangeAtom, SWRLDataRangeAtom {

    /**
     * Constructor.
     * 
     * @param dataRange An {@link org.semanticweb.owlapi.model.OWLDataRange} representing tha cata range atom.
     * @param argument  The atom argument.
     * @param rank     The atom rank.
     */
    public DefaultWSWRLDataRangeAtom(OWLDataRange dataRange, WSWRLDVariable argument, Rank rank) {
        super(dataRange, argument, rank);
        this.iri = dataRange.asOWLDatatype().getIRI();
    }

    /**
     * Constructor without rank (set to {@code null}).
     * 
     * @param dataRange An {@link org.semanticweb.owlapi.model.OWLDataRange} representing tha cata range atom.
     * @param argument  The atom argument.
     */
    public DefaultWSWRLDataRangeAtom(OWLDataRange dataRange, WSWRLDVariable argument) {
        this(dataRange, argument, null);
    }

    public SWRLDArgument getArgument() {
        return (SWRLDArgument) this.argument;
    }

    @Nonnull
    @Override
    public OWLDataRange getPredicate() {
        return (OWLDataRange) super.getPredicate();
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
        if (!(obj instanceof SWRLDataRangeAtom)) {
            return false;
        }
        SWRLDataRangeAtom other = (SWRLDataRangeAtom) obj;
        return other.getArgument().equals(getArgument())
                && other.getPredicate().equals(getPredicate());
    }

    @Override
    protected int compareObjectOfSameType(OWLObject object) {
        SWRLDataRangeAtom other = (SWRLDataRangeAtom) object;
        int diff = getPredicate().compareTo(other.getPredicate());
        if (diff != 0) {
            return diff;
        }
        return getArgument().compareTo(other.getArgument());
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
