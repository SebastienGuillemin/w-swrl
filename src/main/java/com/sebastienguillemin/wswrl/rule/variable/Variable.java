package com.sebastienguillemin.wswrl.rule.variable;

import lombok.Getter;
import lombok.Setter;

public class Variable<T extends Value> {
    @Getter
    private String name;

    @Getter
    @Setter // TODO : tester si une erreur est lancée si mauvais type de valeur.
    private T value;
}
