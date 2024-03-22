package com.sebastienguillemin.wswrl.rule.variable;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.VariableBinding;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.exception.UnknownVariableDomainException;

/**
 * This class bind a variable to a value (represented as a string).
 */
public class DefaultVariableBinding implements VariableBinding {
    private Hashtable<WSWRLVariable, String> bindings; // Variable name -> value's string representation

    public DefaultVariableBinding() {
        this.bindings = new Hashtable<>();
    }

    public void bindLiteral(WSWRLVariable variable, String value) {
        this.bindings.put(variable, value);
    }

    public void bindIndividual(WSWRLVariable variable, IRI value) {
        this.bindings.put(variable, value.toString());
    }

    public String getValue(WSWRLVariable variable) {
        return this.bindings.get(variable);
    }

    @Override
    public String toString() {
        String res = "Variable binding :";

        for (Entry<WSWRLVariable, String> entry : this.bindings.entrySet())
            res += "\n  " + entry.getKey().getIRI().getFragment() + " <- " + entry.getValue();

        return res;
    }

    /**
     * Set variable values.
     * @param individuals
     */
    public void bindVariables(Hashtable<IRI, WSWRLIndividual> individuals) throws UnknownVariableDomainException {
        WSWRLVariable variable;
        String value;
        for (Entry<WSWRLVariable, String> entry : this.bindings.entrySet()) {
            variable = entry.getKey();
            value = entry.getValue();

            if (variable.getDomain() == WSWRLVariableDomain.DATA)
                variable.setValue(value);
            else if (variable.getDomain() == WSWRLVariableDomain.INDIVIDUALS)
                variable.setValue(individuals.get(IRI.create(value)));
            else
                throw new UnknownVariableDomainException(variable.getIRI());
        }
    }
}
