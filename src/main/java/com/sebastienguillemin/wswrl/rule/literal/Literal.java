package com.sebastienguillemin.wswrl.rule.literal;

import java.util.List;

import com.sebastienguillemin.wswrl.rule.variable.Value;
import com.sebastienguillemin.wswrl.rule.variable.Variable;
import com.sebastienguillemin.wswrl.rule.variable.exception.VariableNotFoundException;

public interface Literal {
    public void setWeight(float weight);
    public float getWeight();
    
    public void setVariableValue(String variableName, Value value) throws VariableNotFoundException;
    public List<Variable<Value>> getVariables();

    public void setRank(Rank rank);
    public Rank getRank();

    public boolean test();
    public boolean isValuable();
}