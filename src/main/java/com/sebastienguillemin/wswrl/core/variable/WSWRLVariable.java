package com.sebastienguillemin.wswrl.core.variable;

import org.semanticweb.owlapi.model.SWRLVariable;

public interface WSWRLVariable extends SWRLVariable {
    /**
     * Returns the variable domain.
     * 
     * @see com.sebastienguillemin.wswrl.core.variable.WSWRLVariableDomain
     * @return the variable domain (INDIVIDUAL or DATA).
     */
    public WSWRLVariableDomain getDomain();
}
