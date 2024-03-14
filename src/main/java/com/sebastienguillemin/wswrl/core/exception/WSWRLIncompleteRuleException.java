package com.sebastienguillemin.wswrl.core.exception;

import org.checkerframework.checker.nullness.qual.NonNull;

public class WSWRLIncompleteRuleException extends WSWRLParseException {
    public WSWRLIncompleteRuleException(@NonNull String message) {
        super(message);
    }

}
