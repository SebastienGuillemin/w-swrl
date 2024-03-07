package com.sebastienguillemin.wswrl.rule.literal;

import lombok.Getter;

public abstract class Literal {
    @Getter
    private float weight;

    @Getter
    private Rank rank;

    public void computeWeight() throws Exception {
        throw new Exception("Not implemented yet.");
    }

    public abstract boolean test();
    protected abstract boolean isValuable();    
}