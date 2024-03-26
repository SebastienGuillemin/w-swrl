package com.sebastienguillemin.wswrl.exception;

import org.swrlapi.exceptions.SWRLRuleEngineBridgeException;

public class NoRegisteredWSWRLRuleEnginesException extends SWRLRuleEngineBridgeException {
    /**
     * Default constructor.
     */
    public NoRegisteredWSWRLRuleEnginesException() {
        super("no registered WSWRL rule engines");
    }
}
