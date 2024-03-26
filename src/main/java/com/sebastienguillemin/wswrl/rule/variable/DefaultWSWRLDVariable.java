package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLDVariable extends DefaultWSWRLTypedVariable<OWLLiteral> implements WSWRLDVariable {
    /**
     * Constructor without value.
     * 
     * @param iri The variable IRI.
     */
    public DefaultWSWRLDVariable(IRI iri) {
        super(iri, WSWRLVariableDomain.DATA);
    }

    /**
     * Constructor whith value.
     * 
     * @param iri   The variable IRI.
     * @param value The variable value.
     */
    public DefaultWSWRLDVariable(IRI iri, OWLLiteral value) {
        super(iri, WSWRLVariableDomain.DATA, value);
    }
}