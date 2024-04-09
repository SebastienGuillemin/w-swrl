package com.sebastienguillemin.wswrl.core.rule;

import java.util.Set;

import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;

public interface WSWRLRuleResult {
    Set<WSWRLAtom> getAtoms();
    float getConfidence();
    
}
