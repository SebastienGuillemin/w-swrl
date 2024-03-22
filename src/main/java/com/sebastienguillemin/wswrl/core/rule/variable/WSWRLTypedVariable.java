package com.sebastienguillemin.wswrl.core.rule.variable;

public interface WSWRLTypedVariable<T> extends WSWRLVariable {
    public T getValue();
    public void setValue(T value);
}
