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
    private Set<WSWRLAtom> body;

    @Getter
    private Set<WSWRLAtom> head;

    @Getter
    private float confidence;

    public DefaultWSWRLRule(Set<WSWRLAtom> body, Set<WSWRLAtom> head, boolean enabled) {
        this.body = body;
        this.head = head;
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
