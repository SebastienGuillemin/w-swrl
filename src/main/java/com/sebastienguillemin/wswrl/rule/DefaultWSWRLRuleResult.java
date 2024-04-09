package com.sebastienguillemin.wswrl.rule;

import java.util.Set;

import com.sebastienguillemin.wswrl.core.rule.WSWRLRuleResult;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;

import lombok.Getter;

@Getter
public class DefaultWSWRLRuleResult implements WSWRLRuleResult {
    private Set<WSWRLAtom> atoms;
    private float confidence;

    public DefaultWSWRLRuleResult(Set<WSWRLAtom> atoms, float confidence) {
        this.atoms = atoms;
        this.confidence = confidence;
    }
}
