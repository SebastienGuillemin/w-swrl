package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLTypedVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;

import lombok.Getter;
import lombok.Setter;


/**
 * {@inheritDoc}
 */
public abstract class DefaultWSWRLTypedVariable<T> extends DefaultWSWRLVariable implements WSWRLTypedVariable<T> {
    @Getter
    @Setter
    protected T value;

    public DefaultWSWRLTypedVariable(IRI iri, WSWRLVariableDomain domain) {
        super(iri, domain);
    }

    public DefaultWSWRLTypedVariable(IRI iri, WSWRLVariableDomain domain, T value) {
        this(iri, WSWRLVariableDomain.DATA);
        this.value = value;
    }
}
