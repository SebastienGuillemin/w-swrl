package com.sebastienguillemin.wswrl.engine.target.wrapper;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedObject;

import lombok.Getter;

public abstract class WSWRLAbastractWrapper {
    @Getter
    protected IRI iri;

    protected WSWRLAbastractWrapper(OWLNamedObject owlObject) {
        this.iri = owlObject.getIRI();
    }
}
