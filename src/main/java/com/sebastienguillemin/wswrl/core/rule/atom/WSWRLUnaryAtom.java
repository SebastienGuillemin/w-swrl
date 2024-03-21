package com.sebastienguillemin.wswrl.core.rule.atom;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLArgument;

public interface WSWRLUnaryAtom<A extends WSWRLArgument> extends WSWRLAtom {

    A getWSWRLArgument();
}
