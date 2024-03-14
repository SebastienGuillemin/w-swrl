package com.sebastienguillemin.wswrl.core.exception;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.parser.SWRLParseException;

public class WSWRLParseException extends SWRLParseException {
    public WSWRLParseException() {
        super();
    }

    public WSWRLParseException(@NonNull String s) {
        super(s);
    }
}
