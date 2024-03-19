package com.sebastienguillemin.wswrl.core.exception;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.exceptions.TargetSWRLRuleEngineException;

public class TargetWSWRLRuleEngineException extends TargetSWRLRuleEngineException {
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
