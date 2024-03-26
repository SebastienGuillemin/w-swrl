package com.sebastienguillemin.wswrl.exception;

public class InvalidThresholdException extends Exception {
    public InvalidThresholdException(float threshold) {
        super("Invalid threshold " + threshold + " must in interval ]0; 1[");
    }
}
