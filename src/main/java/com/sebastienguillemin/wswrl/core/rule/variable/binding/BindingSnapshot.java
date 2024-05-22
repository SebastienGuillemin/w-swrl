package com.sebastienguillemin.wswrl.core.rule.variable.binding;

import org.semanticweb.owlapi.model.IRI;

/**
 * Represents a binding snapshot i.e., the variables binding. It is used to
 * archive a binding.
 */
public interface BindingSnapshot extends Comparable<BindingSnapshot> {
    /**
     * Returns the value (possibly {@code null}) of a variable corresponding to the
     * {@code iri} in the snapshot.
     * 
     * @param iri The variable IRI.
     * @return the value (possible {@code null});
     */
    Object getValue(IRI iri);

    void setValue(IRI iri, Object value);
}
