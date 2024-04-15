package com.sebastienguillemin.wswrl.core.rule.variable;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.SWRLArgument;

/**
 * A variable of an WSWRL rule.
 */
public interface WSWRLVariable extends HasIRI, SWRLArgument {
    /**
     * Returns the variable domain.
     * 
     * @see com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain
     * @return the variable domain (INDIVIDUAL or DATA).
     */
    WSWRLVariableDomain getDomain();

    void setUnboundable(boolean boundable);
    boolean isUnboundable();
}

