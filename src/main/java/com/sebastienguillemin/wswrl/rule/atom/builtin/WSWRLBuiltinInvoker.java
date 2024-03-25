package com.sebastienguillemin.wswrl.rule.atom.builtin;

import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.exceptions.SWRLBuiltInException;

import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.exception.BuiltInInvocationException;

public class WSWRLBuiltinInvoker {
    private SWRLBridge bridge;

    public WSWRLBuiltinInvoker(SWRLBridge bridge) {
        this.bridge = bridge;
    }

    public boolean invoke(WSWRLBuiltInAtom builtInAtom) throws BuiltInInvocationException {
        try {
            // If the built-in is evaluated to 'true', method 'invokeSWRLBuiltIn' returns an non-empty list.
            return this.bridge.invokeSWRLBuiltIn(builtInAtom.getRuleName(), builtInAtom.getBuiltInPrefixedName(), 0, false, builtInAtom.getSWRLArguments()).isEmpty();

        } catch (SWRLBuiltInException e) {
            throw new BuiltInInvocationException(builtInAtom.getBuiltInPrefixedName(), e.getMessage());
        }
    }
}
