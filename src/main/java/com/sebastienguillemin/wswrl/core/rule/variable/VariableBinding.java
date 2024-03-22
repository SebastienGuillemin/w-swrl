package com.sebastienguillemin.wswrl.core.rule.variable;

import java.util.Hashtable;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.exception.UnknownVariableDomainException;

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
 * The internal {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable}
 * of a variable is not modified until the {@link #bindVariables} function has
 * been called.
 */
public interface VariableBinding {
    /**
     * Binds an {@link org.semanticweb.owlapi.model.OWLLiteral} string
     * representation
     * to a variable.
     * 
     * @param variable The considered variable.
     * @param value    The value to bind.
     */
    public void bindLiteral(WSWRLVariable variable, String value);

    /**
     * Binds a {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual} IRI.
     * to a
     * variable.
     * 
     * @param variable      The considered variable.
     * @param individualIRI The individual IRI to bind.
     */
    public void bindIndividual(WSWRLVariable variable, IRI individualIRI);

    /**
     * Returns the the string representation of the value (i.e., an
     * {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable} or an
     * {@link org.semanticweb.owlapi.model.OWLLiteral}).
     * 
     * @param variable
     * @return
     */
    public String getValue(WSWRLVariable variable);

    /**
     * Changes the inner state of variables by setting their values according to
     * this binding.
     * 
     * @param individuals
     */
    public void bindVariables(Hashtable<IRI, WSWRLIndividual> individuals) throws UnknownVariableDomainException;

}
