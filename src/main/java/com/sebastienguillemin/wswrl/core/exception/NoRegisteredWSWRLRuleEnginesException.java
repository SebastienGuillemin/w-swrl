package com.sebastienguillemin.wswrl.core.exception;

import org.swrlapi.exceptions.SWRLRuleEngineBridgeException;

public class NoRegisteredWSWRLRuleEnginesException extends SWRLRuleEngineBridgeException {
    public NoRegisteredWSWRLRuleEnginesException() {
        super("no registered WSWRL rule engines");
    }
}
