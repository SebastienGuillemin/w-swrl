package com.sebastienguillemin.wswrl.rule.atom;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.exception.VariableNotFoundException;

import lombok.Getter;
import lombok.Setter;
import uk.ac.manchester.cs.owl.owlapi.SWRLAtomImpl;

public abstract class AbstractWSWRLAtom extends SWRLAtomImpl implements WSWRLAtom {
    @Getter
    @Setter
    protected float weight;

    @Getter
    private Rank rank;

    protected IRI iri;

    protected AbstractWSWRLAtom(SWRLPredicate predicate, Rank rank) {
        super(predicate);
        this.rank = rank;
    }

    @Override
    public IRI getIRI() {
        return this.iri;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        rank.addAtom(this);
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

    @Override
    public String toString() {
        return super.toString() + " Rank:" + this.rank.getIndex();
    }
}
