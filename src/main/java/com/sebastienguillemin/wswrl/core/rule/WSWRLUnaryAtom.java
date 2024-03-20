package com.sebastienguillemin.wswrl.core.rule;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.SWRLArgument;

public interface WSWRLUnaryAtom<A extends SWRLArgument> extends WSWRLAtom {
    /** @return the argument */
    @Nonnull
    A getArgument();
}
