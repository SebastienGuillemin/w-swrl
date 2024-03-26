package com.sebastienguillemin.wswrl.exception;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.exceptions.TargetSWRLRuleEngineException;

public class TargetWSWRLRuleEngineException extends TargetSWRLRuleEngineException {
    /**
     * Default constructor.
     */
    public TargetWSWRLRuleEngineException() {
        super();
    }

    public TargetWSWRLRuleEngineException(@NonNull String message) {
        super(message);
    }

    public TargetWSWRLRuleEngineException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }
}
