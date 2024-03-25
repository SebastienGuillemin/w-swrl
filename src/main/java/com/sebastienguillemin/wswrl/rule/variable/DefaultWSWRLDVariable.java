package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;

public class DefaultWSWRLDVariable extends DefaultWSWRLTypedVariable<OWLLiteral> implements WSWRLDVariable {
    public DefaultWSWRLDVariable(IRI iri) {
        super(iri, WSWRLVariableDomain.DATA);
    }

    public DefaultWSWRLDVariable(IRI iri, OWLLiteral value) {
        super(iri, WSWRLVariableDomain.DATA, value);
    }
}