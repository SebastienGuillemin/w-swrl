package com.sebastienguillemin.wswrl.core.rule;

import org.semanticweb.owlapi.model.OWLAxiom;

public interface WSWRLAxiom {
    public OWLAxiom getAxiom();

    public float getConfidence();
}
