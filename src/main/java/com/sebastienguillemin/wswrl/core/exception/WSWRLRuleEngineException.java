package com.sebastienguillemin.wswrl.core.exception;

import org.swrlapi.exceptions.SWRLRuleEngineException;

public class WSWRLRuleEngineException extends SWRLRuleEngineException {
    public WSWRLRuleEngineException() {
        super();
    }

    public WSWRLRuleEngineException(String message) {
        super(message);
    }

    public WSWRLRuleEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
