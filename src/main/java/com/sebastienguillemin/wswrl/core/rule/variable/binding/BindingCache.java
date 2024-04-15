package com.sebastienguillemin.wswrl.core.rule.variable.binding;

import java.util.List;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;

public interface BindingCache<V extends WSWRLVariable, T> {
    void addValues(V variable, List<T> values);

    /**
     * Indicates whether another binding is available.
     * 
     * @return TRue if another binding is available.
     */
    boolean hasNext();

    /**
     * Update the internal pointers to the next values.
     */
    void next();

    /**
     * Bind the current values (referred by pointers) to variables.
     */
    void bind();

    /**
     * Returns the number of possibilities of bindings.
     * 
     * @return The number of possibilities of bindings.
     */
    int getPossibilities();

    /**
     * Returns the number of possibilities that have been processed.
     * 
     * @return The number of processed possibilities.
     */
    int getProcessedPossibilities();

    /**
     * Returns the current value referrenced by the pointer of the variable
     * identified by the {@code iri} parameter.
     * 
     * @param iri A variable IRI.
     * @return A value that can be bound to the variable referred by {@code iri}.
     */
    T getCurrent(IRI iri);

    /**
     * Locks this cache. This method called {@link #computePossibilities()}.
     */
    void lock();

    /**
     * Releases the lock of the cache.
     */
    void releaseLock();

    /**
     * Returns whether this cache is locked.
     * 
     * @return True if the cache is locked, false otherwise.
     */
    boolean isLocked();

    /**
     * Clear the current cache content.
     */
    void clear();

    /**
     * Compute the binding possibilities of the cache i.e., the cartesian products
     * of all the variable values.
     */
    void computePossibilities();
}
