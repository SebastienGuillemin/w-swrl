package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;

public class DefaultWSWRLIVariable extends DefaultWSWRLTypedVariable<WSWRLIndividual> implements WSWRLIVariable {
    public DefaultWSWRLIVariable(IRI iri) {
        super(iri, WSWRLVariableDomain.INDIVIDUALS);
    }

    public DefaultWSWRLIVariable(IRI iri, WSWRLIndividual value) {
        super(iri, WSWRLVariableDomain.INDIVIDUALS, value);
    }
}
