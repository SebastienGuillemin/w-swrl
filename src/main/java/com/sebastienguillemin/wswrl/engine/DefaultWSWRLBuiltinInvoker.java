package com.sebastienguillemin.wswrl.engine;

import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.exceptions.SWRLBuiltInException;

import com.sebastienguillemin.wswrl.core.engine.WSWRLBuiltinInvoker;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.exception.BuiltInInvocationException;

/**
 * {@inheritDoc}
 * 
 * Singleton class.
 */
public class DefaultWSWRLBuiltinInvoker implements WSWRLBuiltinInvoker {
    private static DefaultWSWRLBuiltinInvoker instance;

    /**
     * Returns the class instance.
     * 
     * @param bridge The SWRL bridge required to create a new instance if no one
     *               exists.
     * @return The class instance.
     */
    public static DefaultWSWRLBuiltinInvoker getInstance(SWRLBridge bridge) {
        if (instance == null)
            instance = new DefaultWSWRLBuiltinInvoker(bridge);
        return instance;
    }

    private SWRLBridge bridge;

    /**
     * Constructor.
     * @param bridge The bridge used to invoke built-ins.
     */
    private DefaultWSWRLBuiltinInvoker(SWRLBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public boolean invoke(WSWRLBuiltInAtom builtInAtom) throws BuiltInInvocationException {
        try {
            // If the built-in is evaluated to 'true', method 'invokeSWRLBuiltIn' returns an
            // non-empty list.
            return this.bridge.invokeSWRLBuiltIn(builtInAtom.getRuleName(), builtInAtom.getBuiltInPrefixedName(), 0,
                    false, builtInAtom.getSWRLArguments()).isEmpty();

        } catch (SWRLBuiltInException e) {
            throw new BuiltInInvocationException(builtInAtom.getBuiltInPrefixedName(), e.getMessage());
        }
    }
}
