package com.sebastienguillemin.wswrl.rule;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.exception.MissingRankException;

import lombok.Getter;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLRule implements WSWRLRule {
    private static final float EPSILON = 0.5f;

    @Getter
    private String ruleName;

    @Getter
    private String comment;

    @Getter
    private boolean enabled;

    @Getter
    private WSWRLAtom head;

    @Getter
    private Set<WSWRLAtom> body;

    private SortedSet<Integer> rankIndexes;
    private Hashtable<Integer, Set<WSWRLAtom>> atRankAtoms;

    @Getter
    private WSWRLAtom atomCausedSkip;

    /**
     * Constructor.
     * 
     * @param ruleName The rule name.
     * @param head     WSWRL head atoms.
     * @param body     WSWRL body atoms.
     * @param enabled  Whether the rule is enable or not.
     * @throws MissingRankException
     */
    public DefaultWSWRLRule(String ruleName, WSWRLAtom head, Set<WSWRLAtom> body, boolean enabled)
            throws MissingRankException {
        this.ruleName = ruleName;
        this.head = head;
        this.body = body;
        this.enabled = enabled;
        this.rankIndexes = new TreeSet<>(); // See cheackRanks method.

        this.atRankAtoms = new Hashtable<>();
        this.processRanks();
    }

    @Override
    public void calculateWeights() {
        float atomWeight;        
        for (int index : this.rankIndexes)  {
            atomWeight = this.calculateAtomsWeight(index);
            
            for (WSWRLAtom atom : this.atRankAtoms.get(index))
                atom.setWeight(atomWeight);
        }
    }

    @Override
    public float calculateConfidence() {
        this.atomCausedSkip = null;

        float truthWeight = 0;
        float falseWeight = 0;
        Iterator<WSWRLAtom> bodyAtoms = this.body.iterator();
        WSWRLAtom atom;
        while (bodyAtoms.hasNext()) {
            atom = bodyAtoms.next();

            if (atom.getRank().getIndex() == 0 && (!atom.isValuable() || !atom.evaluate())) {
                this.atomCausedSkip = atom;
                truthWeight = 0;
                falseWeight = 1;
                break;
            }
            else if(!atom.evaluate()) {
                falseWeight += atom.getWeight();
            }
            else if (!atom.isValuable()) {
                truthWeight += (1 - EPSILON) * atom.getWeight();
                falseWeight += EPSILON * atom.getWeight();
            }
            else if(atom.evaluate())
                truthWeight += atom.getWeight();
        }
        return truthWeight / (truthWeight + falseWeight);
    }

    @Override
    public Set<WSWRLAtom> atRank(int rankIndex) {
        return this.atRankAtoms.get(rankIndex);
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
        int atomRankIndex;
        Set<WSWRLAtom> atoms;
        for (WSWRLAtom atom : this.body) {
            atomRankIndex = atom.getRank().getIndex();
            this.rankIndexes.add(atomRankIndex);

            if (!this.atRankAtoms.containsKey(atomRankIndex)) {
                atoms = new HashSet<>();
                this.atRankAtoms.put(atomRankIndex, atoms);
            } else
                atoms = this.atRankAtoms.get(atomRankIndex);
            atoms.add(atom);
        }

        // Rank 0 can be empty (i.e., no atom assigned to this rank).
        for (int i = 1; i < this.rankIndexes.size(); i++) {
            if (!this.rankIndexes.contains(i))
                throw new MissingRankException(i);
        }
    }

    private float calculateAtomsWeight(int rankIndex) {
        return 1.f / (float) (rankIndex + Math.exp((double) -rankIndex));
    }
}
