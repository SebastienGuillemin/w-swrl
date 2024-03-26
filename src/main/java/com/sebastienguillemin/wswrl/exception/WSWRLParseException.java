package com.sebastienguillemin.wswrl.exception;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.parser.SWRLParseException;

public class WSWRLParseException extends SWRLParseException {
    /**
     * Default constructor.
     */
    public WSWRLParseException() {
        super();
    }

    public WSWRLParseException(@NonNull String s) {
        super(s);
    }
}
