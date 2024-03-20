package com.sebastienguillemin.wswrl.core.rule;

import java.util.List;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.SWRLDArgument;

public interface WSWRLBuiltInAtom extends WSWRLAtom {
    /** @return list of arguments */
    @Nonnull
    List<SWRLDArgument> getArguments();

    /**
     * Determines if the predicate of this atom is is a core builtin.
     * 
     * @return {@code true} if this is a core builtin, otherwise {@code false}
     */
    boolean isCoreBuiltIn();
}
