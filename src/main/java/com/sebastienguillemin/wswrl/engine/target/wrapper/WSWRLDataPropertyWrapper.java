package com.sebastienguillemin.wswrl.engine.target.wrapper;

import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

import lombok.Getter;

public class WSWRLDataPropertyWrapper extends WSWRLAbstractPropertyWrapper<OWLDataPropertyExpression, OWLLiteral> {
    @Getter
    protected OWLLiteral object;

    public WSWRLDataPropertyWrapper(OWLPropertyAssertionAxiom<OWLDataPropertyExpression, OWLLiteral> propertyAssertion)
            throws Exception {
        super(propertyAssertion);
        this.object = propertyAssertion.getObject();
    }

    @Override
    public String toString() {
        return String.format("WSWRLDataPropertyWrapper(%s %s '%s')", this.subject.getIri().toString(),
                this.iri.toString(), this.getObject().getLiteral());
    }

    @Override
    protected void addPropertyToIndividual(WSWRLIndividualWrapper subject) {
        subject.addDataProperty(this);
    }
}
