package com.sebastienguillemin.wswrl.rule.atom.builtin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLPredicate;
import org.swrlapi.builtins.arguments.SWRLBuiltInArgument;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLLiteralBuiltInVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.exception.BuiltInInvocationException;
import com.sebastienguillemin.wswrl.exception.VariableNotFoundException;

import lombok.Getter;
import lombok.Setter;

public class DefaultWSWRLBuiltInAtom implements WSWRLBuiltInAtom {
    @Getter
    @Setter
    private IRI IRI;

    @Getter
    private String builtInPrefixedName;
    private List<WSWRLLiteralBuiltInVariable> variables;
    @Getter
    private Rank rank;

    @Getter
    @Setter
    private float weight;

    public DefaultWSWRLBuiltInAtom(IRI builtInIRI, String builtInPrefixedName,
            List<WSWRLLiteralBuiltInVariable> variables,
            Rank rank) {
        this.IRI = builtInIRI;
        this.rank = rank;
        this.builtInPrefixedName = builtInPrefixedName;
        this.variables = variables;
    }

    public DefaultWSWRLBuiltInAtom(IRI builtInIRI, String builtInPrefixedName,
            List<WSWRLLiteralBuiltInVariable> variables) {
        this(builtInIRI, builtInPrefixedName, variables, null);
    }

    @Override
    public SWRLPredicate getPredicate() {
        return (SWRLPredicate) this.IRI;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        rank.addAtom(this);
    }

    @Override
    public boolean isValuable() {
        for (WSWRLLiteralBuiltInVariable builtInVariable : this.variables) {
            System.err.println("[DefaultWSWRLBuiltInAtom] Variable : " + builtInVariable.getIRI() + " value :" + builtInVariable.getValue());
            if (builtInVariable.getValue() == null)
                return false;
        }

        return true;
    }

    @Override
    public WSWRLVariable getVariable(IRI variableIRI) throws VariableNotFoundException {
        for (SWRLBuiltInArgument argument : this.variables) {
            WSWRLVariable variable = (WSWRLVariable) argument;

            if (variable.getIRI().equals(variableIRI))
                return variable;
        }

        throw new VariableNotFoundException(variableIRI);

    }

    public WSWRLVariable getVariable(String variableName) throws VariableNotFoundException {
        for (SWRLBuiltInArgument argument : this.variables) {
            WSWRLVariable variable = (WSWRLVariable) argument;

            if (variable.getIRI().getFragment().equals(variableName))
                return variable;
        }
        throw new VariableNotFoundException(variableName);
    }

    @Override
    public Set<WSWRLVariable> getVariables() {
        Set<WSWRLVariable> variables = new HashSet<>();

        for (WSWRLLiteralBuiltInVariable argument : this.variables)
            variables.add((WSWRLVariable) argument);

        return variables;
    }

    @Override
    public boolean evaluate() {
        try {
            return WSWRLBuiltinInvoker.invoke(this.builtInPrefixedName, this.variables);
        } catch (BuiltInInvocationException e) {
            e.printStackTrace();
            return false;
        }
    }
}
