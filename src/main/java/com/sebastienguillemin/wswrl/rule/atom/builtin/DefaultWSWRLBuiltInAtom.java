package com.sebastienguillemin.wswrl.rule.atom.builtin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLPredicate;
import org.swrlapi.builtins.arguments.SWRLBuiltInArgument;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.engine.WSWRLBuiltinInvoker;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLLiteralBuiltInVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.exception.BuiltInInvocationException;
import com.sebastienguillemin.wswrl.exception.VariableNotFoundException;

import lombok.Getter;
import lombok.Setter;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLBuiltInAtom implements WSWRLBuiltInAtom {
    @Getter
    @Setter
    private IRI IRI;

    @Getter
    private String ruleName;

    @Getter
    private String builtInPrefixedName;
    private List<WSWRLLiteralBuiltInVariable> variables;
    private WSWRLBuiltinInvoker builtinInvoker;
    @Getter
    private Rank rank;

    @Getter
    @Setter
    private float weight;

    
    public DefaultWSWRLBuiltInAtom(String ruleName, IRI builtInIRI, String builtInPrefixedName,
            List<WSWRLLiteralBuiltInVariable> variables, WSWRLBuiltinInvoker builtinInvoker, 
            Rank rank) {
        this.ruleName = ruleName;
        this.IRI = builtInIRI;
        this.rank = rank;
        this.builtInPrefixedName = builtInPrefixedName;
        this.variables = variables;
        this.builtinInvoker = builtinInvoker;
    }
    
    public DefaultWSWRLBuiltInAtom(String ruleName, IRI builtInIRI, String builtInPrefixedName,
    List<WSWRLLiteralBuiltInVariable> variables, WSWRLBuiltinInvoker builtinInvoker) {
        this(ruleName, builtInIRI, builtInPrefixedName, variables, builtinInvoker, null);
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

    @Override
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
            return this.builtinInvoker.invoke(this);
        } catch (BuiltInInvocationException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public @NonNull List<@NonNull SWRLBuiltInArgument> getSWRLArguments() {
        List<SWRLBuiltInArgument> swrlArgmuents = new ArrayList<>();

        for (WSWRLLiteralBuiltInVariable builtInVariable : this.variables)
            swrlArgmuents.add((SWRLBuiltInArgument) builtInVariable);

        return swrlArgmuents;
    }
}
