package com.sebastienguillemin.wswrl.rule.variable;

import lombok.Getter;
import lombok.Setter;

public class Variable<T extends Value> {
    @Getter
    private String name;

    @Getter
    @Setter
    private T value;
}
