package com.sebastienguillemin.wswrl.rule.atom;

import java.util.Set;

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
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIArgument;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;

public class DefaultWSWRLClassAtom extends AbstractWSWRLUnaryAtom<WSWRLIArgument> implements WSWRLClassAtom, SWRLClassAtom {

    public DefaultWSWRLClassAtom(OWLClassExpression classExpression, WSWRLIArgument argument, Rank rank) {
        super((SWRLPredicate) classExpression, argument, rank);
        this.iri = classExpression.asOWLClass().getIRI();
    }

    public DefaultWSWRLClassAtom(OWLClassExpression classExpression, WSWRLIArgument argument) {
        this(classExpression, argument, null);
    }

    public SWRLIArgument getArgument() {
        return (SWRLIArgument) this.argument;
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
    public void accept(OWLObjectVisitor visitor) {
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
        return this.getWSWRLArgument().getWSWRLIndividual().getOWLClass(this.iri) != null;
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }
}
