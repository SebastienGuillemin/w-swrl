package com.sebastienguillemin.wswrl.exception;

public class MissingRankException extends Exception {
    private int rankIndex;

    public MissingRankException(int rankIndex) {
        this.rankIndex = rankIndex;
    }

    @Override
    public String getMessage() {
        return "Error, missing rank " + this.rankIndex + ". You must use consecutive ranks when definig a rule.";
    }
}