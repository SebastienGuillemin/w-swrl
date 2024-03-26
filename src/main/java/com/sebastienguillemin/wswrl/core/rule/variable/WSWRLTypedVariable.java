package com.sebastienguillemin.wswrl.core.rule.variable;

/**
 * A typed WSWRL variable. The type constraint the variable value and can be any
 * type of object.
 */
public interface WSWRLTypedVariable<T> extends WSWRLVariable {
    /**
     * 
     * @return The variable value.
     */
    public T getValue();

    /**
     *
     * @param value The new variable value.
     */
    public void setValue(T value);
}
