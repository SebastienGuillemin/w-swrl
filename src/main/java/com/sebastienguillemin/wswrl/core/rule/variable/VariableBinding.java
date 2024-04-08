package com.sebastienguillemin.wswrl.core.rule.variable;

/**
 * Represents a binding for the rule variables.
 * A binding associates a value to each variable.
 * 
 * <br>
 * <br>
 * 
 * If the varible type is
 * {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain#INDIVIDUALS}
 * the value is the
 * {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual} IRI.
 * <br>
 * If the varible type is
 * {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain#DATA}
 * the value is the string representation of an
 * {@link org.semanticweb.owlapi.model.OWLLiteral}.
 * 
 * <br>
 * <br>
 * 
 * The internal
 * {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable}
 * of a variable is not modified until the {@link #bindVariables} function has
 * been called.
 */
public interface VariableBinding {

    /**
     * Changes the inner state of variables by setting their values according to
     * this binding.
     */
    public void nextBinding();

    /**
     * Returns wether another binding is remaining.
     * 
     * @return True if another binding is remaining, false otherwise.
     */
    public boolean hasNext();
}
