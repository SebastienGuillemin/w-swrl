package com.sebastienguillemin.wswrl.rule.variable;

import java.util.HashMap;
import java.util.Map.Entry;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import com.sebastienguillemin.wswrl.core.rule.variable.VariableBinding;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;

/**
 * This class bind a variable to a value (represented as a string).
 */
public class DefaultVariableBinding implements VariableBinding {
    private HashMap<WSWRLIVariable, WSWRLIndividual> individualBindings;
    private HashMap<WSWRLDVariable, OWLLiteral> dataBindings;

    public DefaultVariableBinding() {
        this.dataBindings = new HashMap<>();
        this.individualBindings = new HashMap<>();
    }

    public DefaultVariableBinding(DefaultVariableBinding binding) {
        this();

        // TODO : need to use deep copy for hashmap ?
        for (Entry<WSWRLIVariable, WSWRLIndividual> entry : binding.individualBindings.entrySet())
            this.individualBindings.put(entry.getKey(), entry.getValue());

        for (Entry<WSWRLDVariable, OWLLiteral> entry : binding.dataBindings.entrySet())
            this.dataBindings.put(entry.getKey(), entry.getValue());
    }

    @Override
    public void bindLiteral(WSWRLDVariable variable, OWLLiteral value) {
        this.dataBindings.put(variable, value);
    }

    @Override
    public void bindIndividual(WSWRLIVariable variable, WSWRLIndividual value) {
        this.individualBindings.put(variable, value);
    }

    /**
     * Set variable values.
     * 
     * @param individuals
     */
    public void bindVariables() {
        WSWRLIVariable individualVariable;
        WSWRLIndividual individual;
        for (Entry<WSWRLIVariable, WSWRLIndividual> entry : this.individualBindings.entrySet()) {
            individualVariable = entry.getKey();
            individual = entry.getValue();

            individualVariable.setValue(individual);
        }

        WSWRLDVariable dataVariable;
        OWLLiteral data;
        for (Entry<WSWRLDVariable, OWLLiteral> entry : this.dataBindings.entrySet()) {
            dataVariable = entry.getKey();
            data = entry.getValue();

            dataVariable.setValue(data);
        }
    }

    @Override
    public OWLLiteral getLiteralValue(IRI variableIRI) {
        for (Entry<WSWRLDVariable, OWLLiteral> entry : this.dataBindings.entrySet()) {

            if (entry.getKey().getIRI().equals(variableIRI))
                return entry.getValue();
        }

        return null;
    }

    @Override
    public WSWRLIndividual getIndividualValue(IRI variableIRI) {
        for (Entry<WSWRLIVariable, WSWRLIndividual> entry : this.individualBindings.entrySet()) {

            if (entry.getKey().getIRI().equals(variableIRI))
                return entry.getValue();
        }

        return null;
    }

    @Override
    public String toString() {
        String res = "Variable binding :";

        for (Entry<WSWRLIVariable, WSWRLIndividual> entry : this.individualBindings.entrySet())
            res += "\n  " + entry.getKey().getIRI().getFragment() + " <- " + entry.getValue().getIRI();

        for (Entry<WSWRLDVariable, OWLLiteral> entry : this.dataBindings.entrySet()) {
            OWLLiteral literal= entry.getValue();
            res += "\n  " + entry.getKey().getIRI().getFragment() + " <- " + ((literal == null) ? "null" : literal.getLiteral());
        }

        return res;
    }
}
