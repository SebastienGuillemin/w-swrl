package com.sebastienguillemin.wswrl.engine.target.wrapper;

import java.util.Hashtable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLPropertyAssertionObject;
import org.semanticweb.owlapi.model.OWLPropertyExpression;

import lombok.Getter;
import lombok.Setter;

public abstract class WSWRLAbstractPropertyWrapper<P extends OWLPropertyExpression, O extends OWLPropertyAssertionObject> extends WSWRLAbastractWrapper {
    protected OWLPropertyAssertionAxiom<P, O>  propertyAssertion;
    
    @Getter
    @Setter
    protected WSWRLIndividualWrapper subject;

    public IRI getPredicate() {
        return this.getIri();
    }

    // TODO : créer un exception custom.
    protected WSWRLAbstractPropertyWrapper(OWLPropertyAssertionAxiom<P, O> propertyAssertion) throws Exception {
        super((OWLProperty) propertyAssertion.getProperty());

        this.propertyAssertion = propertyAssertion;
    }

    public void parseSubject(Hashtable<IRI, WSWRLIndividualWrapper> individuals) {
        this.subject = this.parseIndividual((OWLNamedIndividual) this.propertyAssertion.getSubject(), individuals);
        this.addPropertyToIndividual(this.subject);
    }

    
    protected WSWRLIndividualWrapper parseIndividual(OWLNamedIndividual namedIndividual, Hashtable<IRI, WSWRLIndividualWrapper> individuals) {
        IRI namedIndividualIRI = namedIndividual.getIRI();
        
        WSWRLIndividualWrapper individualWrapper;
        if (!individuals.containsKey(namedIndividualIRI)) {
            individualWrapper = new WSWRLIndividualWrapper(namedIndividual);
            individuals.put(namedIndividualIRI, individualWrapper);
        } else
        individualWrapper = individuals.get(namedIndividualIRI);
        
        return individualWrapper;
    }
    
    protected abstract void addPropertyToIndividual(WSWRLIndividualWrapper subject);
}
