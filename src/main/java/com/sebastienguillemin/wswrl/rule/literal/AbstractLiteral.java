package com.sebastienguillemin.wswrl.rule.literal;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.sebastienguillemin.wswrl.rule.variable.Value;
import com.sebastienguillemin.wswrl.rule.variable.Variable;
import com.sebastienguillemin.wswrl.rule.variable.exception.VariableNotFoundException;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractLiteral implements Literal{
    @Getter
    @Setter
    protected String name;
    protected Hashtable<String, Variable<Value>> variables;
    protected boolean valuable;

    @Setter
    @Getter
    protected Rank rank;
    
    @Setter
    @Getter
    protected float weight;

    protected AbstractLiteral() {
        this.variables = new Hashtable<>();
        this.valuable = false;
    }
    
    public AbstractLiteral(Rank rank) {
        this();
        this.rank = rank;
    }

    @Override
    public List<Variable<Value>> getVariables() {
        return new ArrayList<>(this.variables.values());
    }  

    @Override
    public void setVariableValue(String variableName, Value value) throws VariableNotFoundException {
        if (!this.variables.containsKey(variableName))
            throw new VariableNotFoundException(variableName);
        else
            this.variables.get(variableName).setValue(value);
    }

    @Override
    public boolean isValuable() {
        for (Variable<Value> variable : this.variables.values()) {
            if (variable.getValue() == null)
                return false;
        }
        return true;
    }
}