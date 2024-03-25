package com.sebastienguillemin.wswrl.exception;

public class BuiltInInvocationException extends Exception {
    public BuiltInInvocationException(String builtinPrefixedName, String reason) {
        super("Error when invoking builtin " + builtinPrefixedName + ". Reason :" + reason);
    }    
}
