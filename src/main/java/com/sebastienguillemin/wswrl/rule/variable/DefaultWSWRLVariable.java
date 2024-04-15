package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;

import lombok.Getter;
import lombok.Setter;
import uk.ac.manchester.cs.owl.owlapi.SWRLVariableImpl;

/**
 * {@inheritDoc}
 */
public abstract class DefaultWSWRLVariable extends SWRLVariableImpl implements WSWRLVariable {
    @Getter
    private WSWRLVariableDomain domain;

    @Getter
    @Setter
    private boolean unboundable;

    protected DefaultWSWRLVariable(IRI iri, WSWRLVariableDomain domain) {
        super(iri);
        this.domain = domain;
    }

    @Override
    public String toString() {
        return String.format("WSWRLVariable(%s, %s)", this.getIRI(), this.domain);
    }
}
