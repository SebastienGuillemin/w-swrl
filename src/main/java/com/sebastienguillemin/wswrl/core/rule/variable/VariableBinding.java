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
    // /**
    //  * Binds an {@link org.semanticweb.owlapi.model.OWLLiteral} string
    //  * representation
    //  * to a variable.
    //  * 
    //  * @param variable The considered variable.
    //  * @param value    The value to bind.
    //  */
    // public void bindLiteral(WSWRLDVariable variable, OWLLiteral value);

    // /**
    //  * Binds a
    //  * {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual} IRI.
    //  * to a
    //  * variable.
    //  * 
    //  * @param variable The considered variable.
    //  * @param value    The individual IRI to bind.
    //  */
    // public void bindIndividual(WSWRLIVariable variable, WSWRLIndividual value);

    // /**
    //  * Return the OWL literal of the variable corresponding to an IRI.
    //  * @param variableIRI The variable IRI.
    //  * @return The variable value (null if the variable does not exist).
    //  */
    // public OWLLiteral getLiteralValue(IRI variableIRI);

    // /**
    //  * Return the WSWRLIndividual of the variable corresponding to an IRI.
    //  * @param variableIRI
    //  * @return The variable value (null if the variable does not exist).
    //  */
    // public WSWRLIndividual getIndividualValue(IRI variableIRI);

    /**
     * Changes the inner state of variables by setting their values according to
     * this binding.
     */
    public void nextBinding();

    public boolean hasNext();
}
