package com.sebastienguillemin.wswrl.exception;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.exceptions.SWRLBuiltInException;

public class WSWRLBuiltInException extends SWRLBuiltInException {
    private static final long serialVersionUID = 1L;

    public WSWRLBuiltInException() {
        super();
    }

    public WSWRLBuiltInException(@NonNull String message) {
        super(message);
    }

    public WSWRLBuiltInException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }
}
