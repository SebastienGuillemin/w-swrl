package com.sebastienguillemin.wswrl.exception;

import org.semanticweb.owlapi.model.IRI;

public class VariableNotFoundException extends Exception {
    private String variableName;

    public VariableNotFoundException(IRI varuableIRI) {
        this.variableName = varuableIRI.toString();
    }

    public VariableNotFoundException(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String getMessage() {
        return "Variable " + this.variableName + " does not exist";
    }
}
