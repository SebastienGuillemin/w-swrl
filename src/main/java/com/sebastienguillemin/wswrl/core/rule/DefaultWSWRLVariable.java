package com.sebastienguillemin.wswrl.core.rule;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.WSWRLVariable;

import uk.ac.manchester.cs.owl.owlapi.SWRLVariableImpl;

public class DefaultWSWRLVariable extends SWRLVariableImpl implements WSWRLVariable {

    public DefaultWSWRLVariable(IRI iri) {
        super(iri);
    }
    
}
