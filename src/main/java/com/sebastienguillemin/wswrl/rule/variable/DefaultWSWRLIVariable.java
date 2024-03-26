package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLIVariable extends DefaultWSWRLTypedVariable<WSWRLIndividual> implements WSWRLIVariable {
    /**
     * Constructor without value.
     * 
     * @param iri The variable IRI.
     */
    public DefaultWSWRLIVariable(IRI iri) {
        super(iri, WSWRLVariableDomain.INDIVIDUALS);
    }

    /**
     * Constructor whith value.
     * 
     * @param iri   The variable IRI.
     * @param value The variable value.
     */
    public DefaultWSWRLIVariable(IRI iri, WSWRLIndividual value) {
        super(iri, WSWRLVariableDomain.INDIVIDUALS, value);
    }
}
