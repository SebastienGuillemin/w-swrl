package com.sebastienguillemin.wswrl.exception;

import org.swrlapi.exceptions.SWRLRuleEngineBridgeException;

public class NoRegisteredWSWRLRuleEnginesException extends SWRLRuleEngineBridgeException {
    public NoRegisteredWSWRLRuleEnginesException() {
        super("no registered WSWRL rule engines");
    }
}
