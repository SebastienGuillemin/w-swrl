package com.sebastienguillemin.wswrl.engine.target.wrapper;

import java.util.Hashtable;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.variable.WSWRLVariable;

/**
 * This class bind a variable to a value (represented as a string).
 */
public class VariableBinding {
    private Hashtable<IRI, String> values; // Variable name -> value's string representation

    public VariableBinding() {
        this.values = new Hashtable<>();
    }

    public void bind(WSWRLVariable variable, String value) {
        this.values.put(variable.getIRI(), value);
    }

    public String getValue(WSWRLVariable variable) {
        return this.values.get(variable.getIRI());
    }
}
