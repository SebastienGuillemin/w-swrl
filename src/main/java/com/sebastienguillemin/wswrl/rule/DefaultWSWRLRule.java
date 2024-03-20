package com.sebastienguillemin.wswrl.rule;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.sebastienguillemin.wswrl.core.rule.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.RankDoesNotExistException;
import com.sebastienguillemin.wswrl.exception.WeightCalculationException;

import lombok.Getter;

public class DefaultWSWRLRule implements WSWRLRule {
    private static final boolean IGNORE_UNVALUABLE_ATOMS = true;
    private static final float EPSILON = 0.01f;

    @Getter
    private String ruleName;

    @Getter
    private String comment;

    @Getter
    private boolean enabled;

    @Getter
    private Set<WSWRLAtom> head;

    @Getter
    private Set<WSWRLAtom> body;

    private SortedSet<Integer> rankIndexes;
    private float[] globalRankWeights;

    public DefaultWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean enabled)
            throws MissingRankException {
        this.ruleName = ruleName;
        this.head = head;
        this.body = body;
        this.enabled = enabled;
        this.rankIndexes = new TreeSet<>(); // See cheackRanks method.

        this.processRanks();

        this.globalRankWeights = new float[rankIndexes.last() + 1];
        this.calculateGlobalWeights();
    }

    private void calculateGlobalWeights() {
        float globalRankWeight;

        int numberOfRanks = this.globalRankWeights.length;
        // Number of ranks whith an index greater than 0.
        int numberOfRanksGT0 = numberOfRanks - 1;

        for (int i = 0; i < numberOfRanks; i++) {
            if (i == 0)
                globalRankWeight = 1;
            else if (i == 1)
                globalRankWeight = numberOfRanksGT0 * (1.0f - EPSILON) / numberOfRanks;
            else
                globalRankWeight = numberOfRanksGT0 * (1.0f - EPSILON - globalRankWeights[i - 1]) / numberOfRanks;

            this.globalRankWeights[i] = globalRankWeight;
        }
    }

    @Override
    public void calculateWeights() throws WeightCalculationException {
        try {
            for (int index : this.rankIndexes) {
                Set<WSWRLAtom> atoms = this.atRank(index);
                int valuableAtomsCount = 0;
                float atomsWeight = 0;

                for (WSWRLAtom atom : atoms) {
                    if (atom.isValuable())
                        valuableAtomsCount++;
                    else
                        atom.setWeight(0);
                }

                atomsWeight = this.calculateAtomsWeight(index, atoms.size(), valuableAtomsCount);

                for (WSWRLAtom atom : atoms) {
                    if (atom.isValuable())
                        atom.setWeight(atomsWeight);
                }
            }
        } catch (RankDoesNotExistException e) {
            throw new WeightCalculationException(e);
        }
    }

    @Override
    public float calculateConfidence() {
        for (WSWRLAtom headAtom : this.head)
            if (!headAtom.evaluate()) {
                return 0;
            }

        float confidence = 1;
        for (WSWRLAtom bodyAtom : this.body)
            if (!bodyAtom.isValuable() || !bodyAtom.evaluate())
                confidence -= bodyAtom.getWeight();

        return confidence;
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

    private void processRanks() throws MissingRankException {
        // Rank 0 necessarily exists but can contain no atom (see below).
        this.rankIndexes.add(0);
        for (WSWRLAtom atom : this.body) {
            this.rankIndexes.add(atom.getRank().getIndex());
        }

        // Rank 0 can be empty (i.e., no atom assigned to this rank).
        for (int i = 1; i < this.rankIndexes.size(); i++) {
            if (!this.rankIndexes.contains(i))
                throw new MissingRankException(i);
        }
    }

    private float calculateAtomsWeight(int rankIndex, int atomsRankCount, int valuableAtomsCount)
            throws RankDoesNotExistException {
        if (rankIndex == 0)
            return 1;
        else if (IGNORE_UNVALUABLE_ATOMS) {
            // Weight calculation policy 1
            return this.getRankGlobalWeight(rankIndex) / ((float) valuableAtomsCount);
        } else {
            // Weight calculation policy 2
            return this.getRankGlobalWeight(rankIndex) / ((float) atomsRankCount);
        }
    }

    private float getRankGlobalWeight(int rankIndex) throws RankDoesNotExistException {
        if (!this.rankIndexes.contains(rankIndex))
            throw new RankDoesNotExistException(rankIndex);

        return this.globalRankWeights[rankIndex];
    }
}
