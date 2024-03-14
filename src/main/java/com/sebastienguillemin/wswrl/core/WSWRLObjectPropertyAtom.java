package com.sebastienguillemin.wswrl.core;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.SWRLIArgument;

public interface WSWRLObjectPropertyAtom extends WSWRLBinaryAtom<SWRLIArgument, SWRLIArgument> {
    /**
     * Gets a simplified form of this atom. This basically creates and returns a
     * new atom where the predicate is not an inverse object property. If the
     * atom is of the form P(x, y) then P(x, y) is returned. If the atom is of
     * the form inverseOf(P)(x, y) then P(y, x) is returned.
     * 
     * @see org.semanticweb.owlapi.model.SWRLObjectPropertyAtom#getSimplified()
     * 
     * @return This atom in a simplified form
     */
    @Nonnull
    WSWRLObjectPropertyAtom getSimplifiedWSWRLObject();
}
