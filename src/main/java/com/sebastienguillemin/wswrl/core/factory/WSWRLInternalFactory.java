package com.sebastienguillemin.wswrl.core.factory;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIInternalFactory;

import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.factory.imp.DefaultWSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.ontology.DefaultWSWRLOntology;

public class WSWRLInternalFactory extends SWRLAPIInternalFactory {
    /**
     * @param iriResolver An IRI resolver
     * @return A SWRLAPI-based OWL data factory
     */
    public static WSWRLDataFactory createWSWRLDataFactory(IRIResolver iriResolver) {
        return new DefaultWSWRLDataFactory(iriResolver);
    }

    /**
     * Create a {@link com.sebastienguillemin.wswrl.core.WSWRLOntology} from an WSWRLAPI-based
     * {@link org.swrlapi.core.SWRLAPIOWLOntology}.
     *
     * @param ontology    An OWLAPI-based ontology
     * @param iriResolver An IRI resolver
     * @return A WSWRL-based wrapper of an OWL ontology
     * @throws SWRLBuiltInException If a SQWRL error occurs during ontology processing
     */
    @NonNull
    public static WSWRLOntology createWSWRLAPIOntology(@NonNull OWLOntology ontology,
            @NonNull IRIResolver iriResolver) throws SWRLBuiltInException {
                WSWRLOntology wswrlOntology = new DefaultWSWRLOntology(ontology, iriResolver);
                wswrlOntology.processOntology();

        return wswrlOntology;
    }
}
