package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;

import lombok.Getter;
import lombok.Setter;

public class DefaultWSWRLIVariable extends DefaultWSWRLVariable implements WSWRLIVariable {
    @Getter
    @Setter
    private WSWRLIndividual value;    

    public DefaultWSWRLIVariable(IRI iri) {
        super(iri, WSWRLVariableDomain.INDIVIDUALS);
    }
}
