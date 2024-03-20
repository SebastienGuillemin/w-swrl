package com.sebastienguillemin.wswrl.core.rule;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.SWRLArgument;

public interface WSWRLBinaryAtom<A extends SWRLArgument, B extends SWRLArgument> extends WSWRLAtom {

    /***
    Gets the
    first argument.**@return
    The second argument*/

    @Nonnull
    A getFirstArgument();

    /**
     * Gets the second argument.
     * 
     * @return The second argument
     */
    @Nonnull
    B getSecondArgument();

}
