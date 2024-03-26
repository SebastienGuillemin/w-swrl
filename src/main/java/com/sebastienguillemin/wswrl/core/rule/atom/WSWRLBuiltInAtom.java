package com.sebastienguillemin.wswrl.core.rule.atom;

import java.util.List;

import org.swrlapi.builtins.arguments.SWRLBuiltInArgument;

/**
 * A WSWRL built-in atom.
 */
public interface WSWRLBuiltInAtom extends WSWRLAtom {
    /**
     * @return The prefixed name of the built-in
     */
    String getBuiltInPrefixedName();

    /**
     * @return the SWRL representation of the built-in arguments.
     */
    List<SWRLBuiltInArgument> getSWRLArguments();

    /**
     * Returns the name of the rule the atom is used in. This function is used to invoke the the built-in.
     * 
     * @see com.sebastienguillemin.wswrl.core.engine.WSWRLBuiltinInvoker
     * 
     * @return The rule name.
     */
    String getRuleName();
}
