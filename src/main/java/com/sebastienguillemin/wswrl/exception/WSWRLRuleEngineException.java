package com.sebastienguillemin.wswrl.exception;

import org.swrlapi.exceptions.SWRLRuleEngineException;

public class WSWRLRuleEngineException extends SWRLRuleEngineException {
    /**
     * Default constructor.
     */
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
