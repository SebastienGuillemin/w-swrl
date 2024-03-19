package com.sebastienguillemin.wswrl.core.engine.target.wrapper;

import java.util.Hashtable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

import lombok.Getter;
import lombok.Setter;

public class WSWRLObjectPropertyWrapper extends WSWRLAbstractPropertyWrapper<OWLObjectPropertyExpression, OWLIndividual> {
    @Getter
    @Setter
    protected WSWRLIndividualWrapper object;

    public WSWRLObjectPropertyWrapper(
            OWLPropertyAssertionAxiom<OWLObjectPropertyExpression, OWLIndividual> propertyAssertion) throws Exception {
        super(propertyAssertion);
    }

    public void parseObject(Hashtable<IRI, WSWRLIndividualWrapper> individuals) {
        this.object = this.parseIndividual((OWLNamedIndividual) this.propertyAssertion.getObject(), individuals);
    }

    @Override
    public String toString() {
        return String.format("WSWRLObjectPropertyWrapper(%s %s '%s')", this.subject.getIri().toString(),
                this.iri.toString(), this.getObject().getIri());
    }

    @Override
    protected void addPropertyToIndividual(WSWRLIndividualWrapper subject) {
        subject.addObjectProperty(this);
    }
}
