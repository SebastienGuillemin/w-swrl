package com.sebastienguillemin.wswrl.core.rule.atom;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;

public interface WSWRLPropertyAtom<ObjectType extends WSWRLVariable> extends WSWRLAtom {

    WSWRLIVariable getFirstWSWRLArgument();

    ObjectType getSecondWSWRLArgument();
}
