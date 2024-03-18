package com.sebastienguillemin.wswrl.core.rule;

import java.util.HashSet;
import java.util.Set;

import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLRule;

import lombok.Getter;
import lombok.Setter;

public class DefaultWSWRLRule implements WSWRLRule {
    @Getter
    private String ruleName;

    @Getter
    private String comment;

    @Getter
    @Setter
    private boolean enabled;

    @Getter
    private Set<WSWRLAtom> head;

    @Getter
    private Set<WSWRLAtom> body;

    @Getter
    private float confidence;

    public DefaultWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean enabled) {
        this.ruleName = ruleName;
        this.head = head;
        this.body = body;
        this.enabled = enabled;
    }
    @Override
    public void computeWeights() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'computeWeights'");
    }

    @Override
    public Set<WSWRLAtom> atRank(int rankIndex) {
        Set<WSWRLAtom> atRankAtoms = new HashSet<>();

        for (WSWRLAtom atom : this.body) {
            if (atom.getRank().getIndex() == rankIndex)
                atRankAtoms.add(atom);
        }

        return atRankAtoms;
    }

    @Override
    public Set<WSWRLAtom> valuable(Set<WSWRLAtom> atoms) {
        Set<WSWRLAtom> valuableAtoms = new HashSet<>();

        for (WSWRLAtom atom : this.body) {
            if (atom.isValuable())
                valuableAtoms.add(atom);
        }

        return valuableAtoms;
    }
}
