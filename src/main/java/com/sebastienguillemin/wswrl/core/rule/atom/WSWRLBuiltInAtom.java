package com.sebastienguillemin.wswrl.core.rule.atom;

import java.util.List;

import javax.annotation.Nonnull;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;

public interface WSWRLBuiltInAtom extends WSWRLAtom {
    /**
     * @return list of arguments
     */
    @Nonnull
    List<WSWRLVariable> getArguments();
}
