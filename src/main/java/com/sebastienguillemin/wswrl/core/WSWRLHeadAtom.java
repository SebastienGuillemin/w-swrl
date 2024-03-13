package com.sebastienguillemin.wswrl.core;

/**
 * A {@link com.sebastienguillemin.wswrl.core.WSWRLRule} head atom that is associated to a confidence ranging in [0; 1].
 * 
 * @see com.sebastienguillemin.wswrl.core.WSWRLAtom
 */
public interface WSWRLHeadAtom extends WSWRLAtom {
    /**
     * @param confidence The head confidence
     */
    public void setConfidence(float confidence);

    /**
     * @return The head confidence.
     */
    public float getConfidence();
}
