package com.sebastienguillemin.wswrl.core.engine;

import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.exception.BuiltInInvocationException;

/**
 * This interface must be implemented by a built-in invoker. A built-in invoker
 * is used to called built-in implementation from the SWRLAPI librairy.
 * 
 * @see com.sebastienguillemin.wswrl.core.rule.atom.WSWRLBuiltInAtom
 */
public interface WSWRLBuiltinInvoker {
    /**
     * Invokes the buitlin using the SWRL API librairy.
     * 
     * @see org.swrlapi.bridge.SWRLBridge
     * 
     * @param builtInAtom The built-in atom to invoke.
     * @return Whether or not the built-in is satisfied.
     * @throws BuiltInInvocationException If an error occurs.
     */
    public boolean invoke(WSWRLBuiltInAtom builtInAtom) throws BuiltInInvocationException;
}
