package com.sebastienguillemin.wswrl.exception;

import org.semanticweb.owlapi.model.IRI;

public class UnknownVariableDomainException extends Exception {
    public UnknownVariableDomainException(IRI variableIRI) {
        super(variableIRI.toString());
    }
}
