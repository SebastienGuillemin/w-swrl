package com.sebastienguillemin.wswrl.engine.target.wrapper;

import java.util.Hashtable;

import org.semanticweb.owlapi.model.OWLLiteral;

import com.sebastienguillemin.wswrl.core.WSWRLVariable;

public class VariableBinding {
    private Hashtable<WSWRLVariable, WSWRLIndividualWrapper> individualValuedVariables;
    private Hashtable<WSWRLVariable, OWLLiteral> dataValuedVariables;

    public VariableBinding() {
        this.individualValuedVariables = new Hashtable<>();
        this.dataValuedVariables = new Hashtable<>();
    }
}
