package com.sebastienguillemin.wswrl.core.rule;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.WSWRLVariableDomain;

import lombok.Getter;
import uk.ac.manchester.cs.owl.owlapi.SWRLVariableImpl;

public class DefaultWSWRLVariable extends SWRLVariableImpl implements WSWRLVariable {
    @Getter
    private WSWRLVariableDomain domain;

    public DefaultWSWRLVariable(IRI iri, WSWRLVariableDomain domain) {
        super(iri);
        this.domain = domain;
    }
    
    @Override
    public String toString() {
        return String.format("WSWRLVariable(%s, %s)", this.getIRI(), this.domain);
    }
}
