package com.sebastienguillemin.wswrl.factory;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIInternalFactory;

import com.sebastienguillemin.wswrl.core.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngineFactory;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngineManager;
import com.sebastienguillemin.wswrl.ontology.DefaultWSWRLOntology;

public class WSWRLInternalFactory extends SWRLAPIInternalFactory {
    private static final WSWRLRuleEngineFactory ruleEngineFactory;

    static {
        ruleEngineFactory = new DefaultWSWRLRuleEngineFactory();
    }

    /**
     * @param iriResolver An IRI resolver
     * @return A SWRLAPI-based OWL data factory
     */
    public static WSWRLDataFactory createWSWRLDataFactory(IRIResolver iriResolver) {
        return new DefaultWSWRLDataFactory(iriResolver);
    }

    /**
     * Create a {@link com.sebastienguillemin.wswrl.core.WSWRLOntology} from an
     * WSWRLAPI-based
     * {@link org.swrlapi.core.SWRLAPIOWLOntology}.
     *
     * @param ontology    An OWLAPI-based ontology
     * @param iriResolver An IRI resolver
     * @return A WSWRL-based wrapper of an OWL ontology
     * @throws SWRLBuiltInException If a SQWRL error occurs during ontology
     *                              processing
     */
    public static WSWRLOntology createWSWRLAPIOntology(OWLOntology ontology,
            IRIResolver iriResolver) throws SWRLBuiltInException {
        WSWRLOntology wswrlOntology = new DefaultWSWRLOntology(ontology, iriResolver);
        wswrlOntology.processOntology();

        return wswrlOntology;
    }

    public static WSWRLRuleEngineFactory getWSWRLRuleEngineFactory() {
        return WSWRLInternalFactory.ruleEngineFactory;
    }

    /**
     * @return A SWRL rule engine manager
     */
    public static WSWRLRuleEngineManager createWSWRLRuleEngineManager() {
        return new DefaultWSWRLRuleEngineManager();
    }
}
