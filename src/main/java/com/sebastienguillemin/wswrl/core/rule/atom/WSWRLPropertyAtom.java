package com.sebastienguillemin.wswrl.core.rule.atom;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLArgument;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIArgument;

public interface WSWRLPropertyAtom<ObjectType extends WSWRLArgument> extends WSWRLAtom {

    WSWRLIArgument getFirstWSWRLArgument();

    ObjectType getSecondWSWRLArgument();
}
