package com.sebastienguillemin.wswrl.core.rule.atom;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;
import com.sebastienguillemin.wswrl.core.exception.VariableNotFoundException;

import lombok.Getter;
import lombok.Setter;
import uk.ac.manchester.cs.owl.owlapi.SWRLAtomImpl;

public abstract class AbstractWSWRLAtom extends SWRLAtomImpl implements WSWRLAtom {
    @Getter
    @Setter
    protected float weight;

    @Getter
    private Rank rank;

    public void setRank(Rank rank) throws AlreadyInRankException {
        this.rank = rank;
        rank.addAtom(this);
    }

    protected AbstractWSWRLAtom(SWRLPredicate predicate, Rank rank) {
        super(predicate);
        this.rank = rank;
    }

    @Override
    public String toString() {
        return super.toString() + " Rank:" + this.rank.getIndex();
    }

    @Override
    public WSWRLVariable getVariable(IRI variableIRI) throws VariableNotFoundException {
        for (SWRLArgument argument : this.getAllArguments())
            if (argument instanceof WSWRLVariable) {
                WSWRLVariable variable = (WSWRLVariable)argument;

                if (variable.getIRI().equals(variableIRI))
                    return variable;
            }

        
        throw new VariableNotFoundException(variableIRI);
    }

    public WSWRLVariable getVariable(String variableName) throws VariableNotFoundException {
        for (SWRLArgument argument : this.getAllArguments())
            if (argument instanceof WSWRLVariable) {
                WSWRLVariable variable = (WSWRLVariable)argument;

                if (variable.getIRI().getFragment().equals(variableName))
                    return variable;
            }

        
        throw new VariableNotFoundException(variableName);
    }

    @Override
    public Set<WSWRLVariable> getVariables() {
        Set<WSWRLVariable> variables = new HashSet<>();

        for (SWRLArgument argument : this.getAllArguments()) {
            if (argument instanceof WSWRLVariable)
                variables.add((WSWRLVariable) argument);
        }

        return variables;
    }
}
