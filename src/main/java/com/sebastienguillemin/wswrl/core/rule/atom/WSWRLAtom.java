package com.sebastienguillemin.wswrl.core.rule.atom;

import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.exception.VariableNotFoundException;

/**
 * A {@link com.sebastienguillemin.wswrl.core.rule.WSWRLRule} atom associated with a
 * weight ranging in [0; 1].
 * 
 * @see org.semanticweb.owlapi.model.SWRLAtom
 */
public interface WSWRLAtom extends HasIRI {
    /**
     * Gets the predicate of this atom.
     * 
     * @return The atom predicate
     */
    @Nonnull
    SWRLPredicate getPredicate();

    /**
     * 
     * @param weight The atom weight.
     */
    public void setWeight(float weight);

    /**
     * 
     * @return The atom weight.
     */
    public float getWeight();

    /**
     * Set the rank of the current atom.
     * 
     * @param rank The atom rank.
     */
    public void setRank(Rank rank);

    /**
     * 
     * @return The atom rank.
     */
    public Rank getRank();

    /**
     * Return weither the current atom is valuable.
     * 
     * @return {@code True} if the atom is valuable {@code False} otherwise.
     */
    public boolean isValuable();

    /**
     * Returns the {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable} corresponding to an IRI.
     * 
     * @param variableIRI the variable IRI.
     * @return the variable.
     * @throws VariableNotFoundException If no variable corresponds to the IRI.
     */
    public WSWRLVariable getVariable(IRI variableIRI) throws VariableNotFoundException;

    /**
     * Returns the {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable} corresponding to a name.
     * 
     * @param variableName the variable name.
     * @return the variable.
     * @throws VariableNotFoundException If no variable corresponds to the name.
     */
    public WSWRLVariable getVariable(String variableName) throws VariableNotFoundException;

    /**
     * Returns all the atom variables.
     * @return the set of variable (possibly empty).
     */
    public Set<WSWRLVariable> getVariables();

    /**
     * Returns the true value (i.e., {@code True} or {@code False}) of the current
     * atom for a certain valuation.
     * 
     * @return The atom truth value.
     */
    public boolean evaluate();
}
