package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;

import lombok.Getter;
import lombok.Setter;

public class DefaultWSWRLDVariable extends DefaultWSWRLVariable implements WSWRLDVariable {
    @Getter
    @Setter
    private OWLLiteral value;    

    public DefaultWSWRLDVariable(IRI iri) {
        super(iri, WSWRLVariableDomain.DATA);
    }
}