package com.sebastienguillemin.wswrl.core.rule.variable.binding;

import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;

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
 * of a variable is not modified until the {@link #nextBinding()} function has
 * been called.
 */
public interface VariableBinding {

    /**
     * Changes the inner state of variables by setting their values according to
     * this binding.
     */
    void nextBinding();

    /**
     * Returns wether another binding is remaining.
     * 
     * @return True if another binding is remaining, false otherwise.
     */
    boolean hasNext();

    /**
     * Returns the number of possibilities i.e., the cardinality of the Cartesian
     * product of individuals who can be subjects of predicates
     * 
     * @return The binding possiblities
     */
    int getBindingPossibilities();

    /**
     * Skip one or several binding according to an atom.
     * 
     * @see WSWRLRule#getAtomCausedSkip()
     * 
     * @param atomCausedSkip The atom that caused the skip.
     */
    void skipByCause(WSWRLAtom atomCausedSkip);

    /**
     * Skip the current binding to the next subject individuals binding.
     */
    void skipBinding();
}
