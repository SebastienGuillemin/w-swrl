package com.sebastienguillemin.wswrl.core.rule.variable;

import org.semanticweb.owlapi.model.HasIRI;

public interface WSWRLVariable extends WSWRLDArgument, WSWRLIArgument, HasIRI {
    /**
     * Returns the variable domain.
     * 
     * @see com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain
     * @return the variable domain (INDIVIDUAL or DATA).
     */
    public WSWRLVariableDomain getDomain();

    public Object getValue();

    public void setValue(Object value);
}
