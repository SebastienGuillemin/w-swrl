package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;

import lombok.Getter;
import uk.ac.manchester.cs.owl.owlapi.SWRLVariableImpl;

public class DefaultWSWRLVariable extends SWRLVariableImpl implements WSWRLVariable {
    @Getter
    private WSWRLVariableDomain domain;

    @Getter
    private Object value;

    public DefaultWSWRLVariable(IRI iri, WSWRLVariableDomain domain) {
        super(iri);
        this.domain = domain;
    }

    public void setValue(Object value) {
        System.out.println(value.getClass());
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.format("WSWRLVariable(%s, %s)", this.getIRI(), this.domain);
    }

    @Override
    public WSWRLIndividual getWSWRLIndividual() {
        return (WSWRLIndividual) this.value;
    }
}
