package com.sebastienguillemin.wswrl.core.factory;

import org.swrlapi.core.IRIResolver;
import org.swrlapi.factory.SWRLAPIInternalFactory;

import com.sebastienguillemin.wswrl.core.factory.imp.DefaultWSWRLDataFactory;

public class WSWRLInternalFactory extends SWRLAPIInternalFactory {
    /**
     * @param iriResolver An IRI resolver
     * @return A SWRLAPI-based OWL data factory
     */
    public static WSWRLDataFactory createWSWRLDataFactory(IRIResolver iriResolver) {
        return new DefaultWSWRLDataFactory(iriResolver);
    }
}
