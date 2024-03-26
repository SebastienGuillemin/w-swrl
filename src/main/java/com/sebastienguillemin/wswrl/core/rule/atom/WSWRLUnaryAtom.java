package com.sebastienguillemin.wswrl.core.rule.atom;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;

/**
 * A WSWRL unary atom. The atom argument must extends
 * {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable}.
 */
public interface WSWRLUnaryAtom<A extends WSWRLVariable> extends WSWRLAtom {

    /**
     * 
     * @return The atom argument.
     */
    public A getWSWRLArgument();
}
