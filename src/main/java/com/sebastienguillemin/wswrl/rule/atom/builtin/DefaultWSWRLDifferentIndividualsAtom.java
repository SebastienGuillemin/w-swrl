package com.sebastienguillemin.wswrl.rule.atom.builtin;

import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDifferentIndividualsAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.rule.atom.property.AbstractWSWRLProperty;

public class DefaultWSWRLDifferentIndividualsAtom extends AbstractWSWRLProperty<WSWRLIVariable> implements WSWRLDifferentIndividualsAtom {

    protected DefaultWSWRLDifferentIndividualsAtom(OWLPropertyExpression property, WSWRLIVariable firstArgument,
            WSWRLIVariable secondArgument, Rank rank) {
        super(property, firstArgument, secondArgument, rank);
    }

    @Override
    public boolean isValuable() {
        return this.firstArgument.getValue() != null && this.secondArgument.getValue() != null;

    }

    @Override
    public boolean evaluate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }

    @Override
    public void accept(SWRLObjectVisitor visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }

    @Override
    public <O> O accept(SWRLObjectVisitorEx<O> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }

    @Override
    public void accept(OWLObjectVisitor visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }

    @Override
    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }

    @Override
    protected int index() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'index'");
    }
    
}
