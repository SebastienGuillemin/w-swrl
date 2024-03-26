package com.sebastienguillemin.wswrl.core.rule.atom;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;

/**
 * A WSWRL property atom. This interface defines methods used by WSWRL property
 * atoms. The object type of a property must extends
 * {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable}.
 * 
 * @see com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom
 * @see com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom
 * @see com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDifferentIndividualsAtom
 * @see com.sebastienguillemin.wswrl.core.rule.atom.WSWRLSameIndividualsAtom
 */
public interface WSWRLPropertyAtom<ObjectType extends WSWRLVariable> extends WSWRLAtom {
    /**
     * Return the first argument of a property which is always an individual (i.e.,
     * {@link com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable}).
     * 
     * @return The property subject.
     */
    WSWRLIVariable getSubject();

    /**
     * The object type depends on the {@code ObjectType} type.
     * 
     * @return The The property object.
     */
    ObjectType getObject();
}
