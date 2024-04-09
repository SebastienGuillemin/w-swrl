package com.sebastienguillemin.wswrl.core.rule.variable.binding;

import java.util.HashMap;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;

public interface BindingCache<V extends WSWRLVariable, T> {
    void init(HashMap<V, List<T>> variablesValues);
    void addValues(V variable, List<T> values);
    boolean hasNext();
    void next();
    void bind();
    int getPossibilities();
    int getProcessedPossibilities();
    T getCurrent(IRI iri);
    void lock();
    void releaseLock();
    boolean isLocked();
    void clear();

    void computePossibilities();
}
