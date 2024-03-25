package com.sebastienguillemin.wswrl.core.rule.atom;

import java.util.List;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.builtins.arguments.SWRLBuiltInArgument;

public interface WSWRLBuiltInAtom extends WSWRLAtom {
    /**
     * @return The prefixed name of the built-in
     */
    String getBuiltInPrefixedName();

    @NonNull
    List<@NonNull SWRLBuiltInArgument> getSWRLArguments();

    String getRuleName();
}
