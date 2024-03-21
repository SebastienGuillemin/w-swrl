package com.sebastienguillemin.wswrl.core.rule.variable;

import lombok.Getter;

public enum WSWRLVariableDomain {
    INDIVIDUALS("Individuals domain"), DATA("Datavalued domain"), UNKNOWN("Unknown domain");

    @Getter
    private String description;

    private WSWRLVariableDomain(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Domain : %s", this.description);
    }
}