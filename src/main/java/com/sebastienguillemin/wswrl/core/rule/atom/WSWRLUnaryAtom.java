package com.sebastienguillemin.wswrl.core.rule.atom;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;

public interface WSWRLUnaryAtom<A extends WSWRLVariable> extends WSWRLAtom {

    public A getWSWRLArgument();
}
