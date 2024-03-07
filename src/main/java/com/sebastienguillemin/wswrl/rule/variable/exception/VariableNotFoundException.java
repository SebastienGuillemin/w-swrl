package com.sebastienguillemin.wswrl.rule.variable.exception;

import lombok.Getter;

@Getter
public class VariableNotFoundException extends Exception {
    private String variableName;

    public VariableNotFoundException (String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String getMessage() {
        return String.format("Variable %s not found.", this.variableName);
    }
}
