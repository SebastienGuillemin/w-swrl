package com.sebastienguillemin.wswrl.core.rule;

import java.util.ArrayList;
import java.util.List;

import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLHeadAtom;
import com.sebastienguillemin.wswrl.core.WSWRLRule;

import lombok.Getter;
import lombok.Setter;

public class DefaultSWRLRule implements WSWRLRule {
    @Getter
    private String ruleName;

    @Getter
    private String comment;

    @Getter
    @Setter
    private boolean active;

    @Getter
    private List<WSWRLAtom> bodyAtoms;

    @Getter
    private WSWRLHeadAtom headAtom;

    @Override
    public void computeWeights() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'computeWeights'");
    }

    @Override
    public List<WSWRLAtom> atRank(int rankIndex) {
        List<WSWRLAtom> atRankAtoms = new ArrayList<>();

        for (WSWRLAtom atom : this.bodyAtoms) {
            if (atom.getRank().getIndex() == rankIndex)
                atRankAtoms.add(atom);
        }

        return atRankAtoms;
    }

    @Override
    public List<WSWRLAtom> valuable(List<WSWRLAtom> atoms) {
        List<WSWRLAtom> valuableAtoms = new ArrayList<>();

        for (WSWRLAtom atom : this.bodyAtoms) {
            if (atom.isValuable())
                valuableAtoms.add(atom);
        }

        return valuableAtoms;
    }
}
