package com.sebastienguillemin.wswrl.rule;

import org.semanticweb.owlapi.model.OWLAxiom;

import com.sebastienguillemin.wswrl.core.rule.WSWRLAxiom;

import lombok.Getter;


public class DefaultWSWRLAxiom implements Comparable<DefaultWSWRLAxiom>, WSWRLAxiom {
    private OWLAxiom axiom;
    private float confidence;

    public DefaultWSWRLAxiom(OWLAxiom axiom, float confidence) {
        this.axiom = axiom;
        this.confidence = confidence;
    }

    @Override
    public OWLAxiom getAxiom() {
        return this.axiom;
    }

    @Override
    public float getConfidence() {
        return this.confidence;
    }

    @Override
    public String toString() {
        return this.axiom.toString() + ", Confidence : " + this.confidence;
    }

    @Override
    public int compareTo(DefaultWSWRLAxiom other) {
        return this.axiom.compareTo(other.getAxiom());
    }
}