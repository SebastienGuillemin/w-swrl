package com.sebastienguillemin.wswrl.exception;

public class RankDoesNotExistException extends Exception {
    private int rankIndex;

    public RankDoesNotExistException(int rankIndex) {
        this.rankIndex = rankIndex;
    }
    

    @Override
    public String toString() {
        return String.format("Rankg with index %s does not exist.", this.rankIndex);
    }
}
