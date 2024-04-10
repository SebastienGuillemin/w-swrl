package com.sebastienguillemin.wswrl.rule.variable.binding;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.binding.BindingCache;

import lombok.Getter;

public class DefaultBindingCache<V extends WSWRLVariable, T> implements BindingCache<V, T> {
    private HashMap<IRI, V> variables;      // Variables IRI -> variables
    private HashMap<IRI, List<T>> values;   // Variables IRI -> possible values (individuals or data)
    private HashMap<IRI, Integer> pointers; // Variables IRI -> integer (pointer)

    @Getter
    private int possibilities;

    @Getter
    private int processedPossibilities;

    @Getter
    private boolean locked;

    public DefaultBindingCache() {
        this.variables = new HashMap<>();
        this.values = new HashMap<>();
        this.pointers = new HashMap<>();
        this.possibilities = 1;
        this.locked = false;
    }

    public DefaultBindingCache(HashMap<V, List<T>> variablesValues) {
        this();
        
        for (Entry<V, List<T>> entry : variablesValues.entrySet()) {
            this.addValues(entry.getKey(), entry.getValue());
        }
        this.processedPossibilities = 0; 
    }

    @Override
    public void addValues(V variable, List<T> values) {
        IRI iri = variable.getIRI();
        if (this.values.containsKey(iri))
            this.values.get(iri).addAll(values);
        else {
            this.values.put(iri, values);
            this.variables.put(iri, variable);
            this.pointers.put(iri, 0);
        }

        if (values.size() == 0)
            variable.setUnboundable(true);
        // System.out.println("Adding : " + variable + " -> " + values);
    }

    @Override
    public void computePossibilities() {
        if (this.values.size() == 0) {
            this.possibilities = 0;
            return;
        }
        
        this.possibilities = 1;
        for (Entry<IRI, List<T>> entry : this.values.entrySet())
            this.possibilities *= entry.getValue().size();
    }

    @Override
    public boolean hasNext() {
        return this.processedPossibilities < this.possibilities;
    }

    @Override
    public void next() {
        if (this.processedPossibilities > 0) {
            int pointer = 0;
            for (IRI variableIRI : this.values.keySet()) {
                pointer = this.pointers.get(variableIRI);
                pointer = ++pointer % this.values.get(variableIRI).size();
                this.pointers.put(variableIRI, pointer);
    
                if (pointer != 0)
                    break;
            }
        }
        this.processedPossibilities++; 

        if (!this.hasNext()) {
            this.releaseLock();
            return;
        }
    }

    @Override
    public void bind() {
        IRI iri;
        V variable;
        WSWRLIVariable iVariable;
        WSWRLDVariable dVariable;
        int pointer;
        T value;
        List<T> variableValues;
        for (Entry<IRI, V> entry : this.variables.entrySet()) {
            iri = entry.getKey();
            variable = entry.getValue();
            pointer = this.pointers.get(iri);
            variableValues = this.values.get(iri);
            value =  (variableValues == null || variableValues.size() == 0) ? null : variableValues.get(pointer);

            // TODO : optimiser en remontant 'setValue' dans l'interface WSWRLVariable ?
            if (variable instanceof WSWRLIVariable) {
                iVariable = (WSWRLIVariable) variable;
                iVariable.setValue((WSWRLIndividual) value);
            } else {
                dVariable = (WSWRLDVariable) variable;
                dVariable.setValue((OWLLiteral) value);
            }
        }
    }

    @Override
    public T getCurrent(IRI iri) {
        return this.values.get(iri).get(this.pointers.get(iri));
    }

    @Override
    public void lock() {
        this.computePossibilities();
        this.locked = true;
    }

    @Override
    public void releaseLock() {
        this.locked = false;
    }

    @Override
    public void clear() {
        this.variables.clear();
        this.values.clear();
        this.pointers.clear();
        this.possibilities = 1;
        this.processedPossibilities = 0;
        this.locked = false;
    }

    @Override
    public String toString() {
       return this.values.toString();
    }
}
