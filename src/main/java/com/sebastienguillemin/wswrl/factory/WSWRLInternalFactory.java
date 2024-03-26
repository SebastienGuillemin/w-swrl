package com.sebastienguillemin.wswrl.factory;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.DefaultSWRLBridge;
import org.swrlapi.factory.SWRLAPIInternalFactory;
import org.swrlapi.owl2rl.OWL2RLPersistenceLayer;

import com.sebastienguillemin.wswrl.core.engine.WSWRLBuiltinInvoker;
import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngineManager;
import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.factory.WSWRLRuleEngineFactory;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.engine.DefaultWSWRLBuiltinInvoker;
import com.sebastienguillemin.wswrl.engine.DefaultWSWRLRuleEngineManager;
import com.sebastienguillemin.wswrl.ontology.DefaultWSWRLOntology;

/**
 * Static factory that creates requirements for other factories. Extends {@link org.swrlapi.factory.SWRLAPIInternalFactory}.
 * 
 * @see com.sebastienguillemin.wswrl.factory.DefaultWSWRLDataFactory
 * @see com.sebastienguillemin.wswrl.factory.DefaultWSWRLRuleEngineFactory
 * @see com.sebastienguillemin.wswrl.factory.WSWRLFactory
 */
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
     * Create a {@link com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology}
     * from an
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

    /**
     * Creates a
     * {@link com.sebastienguillemin.wswrl.engine.DefaultWSWRLBuiltinInvoker} from
     * an ontolory. DefaultWSWRLBuiltinInvoker instance is a singleton instance.
     * 
     * @param ontology The ontology used to create the built-in invoker.
     * @return A built-in invoer instance.
     */
    public static WSWRLBuiltinInvoker getWSWRLlBuiltinInvoker(WSWRLOntology ontology) {
        OWL2RLPersistenceLayer owl2RLPersistenceLayer = WSWRLInternalFactory
                .createOWL2RLPersistenceLayer(ontology.getOWLOntology());
        SWRLBridge bridge = new DefaultSWRLBridge(ontology, owl2RLPersistenceLayer);
        WSWRLBuiltinInvoker builtinInvoker = DefaultWSWRLBuiltinInvoker.getInstance(bridge);
        return builtinInvoker;
    }
}
