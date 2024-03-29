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
 * Static factory that creates requirements for other factories. Extends
 * {@link org.swrlapi.factory.SWRLAPIInternalFactory}.
 * 
 * @see com.sebastienguillemin.wswrl.factory.DefaultWSWRLDataFactory
 * @see com.sebastienguillemin.wswrl.factory.DefaultWSWRLRuleEngineFactory
 * @see com.sebastienguillemin.wswrl.factory.WSWRLFactory
 */
public class WSWRLInternalFactory extends SWRLAPIInternalFactory {
    private static final WSWRLRuleEngineFactory ruleEngineFactory;
    private static SWRLBridge swrlBridge;

    static {
        ruleEngineFactory = new DefaultWSWRLRuleEngineFactory();
    }

    /**
     * Returns a bridge instance. This instance is a singleton and ensures that a
     * unique bridge is used in the application.
     * 
     * @param ontology THe ontology used to create the bridge.
     * @return The bridge instance.
     */
    public static SWRLBridge getBridge(WSWRLOntology ontology) {
        if (swrlBridge == null) {
            OWL2RLPersistenceLayer owl2RLPersistenceLayer = WSWRLInternalFactory
                    .createOWL2RLPersistenceLayer(ontology.getOWLOntology());
            swrlBridge = new DefaultSWRLBridge(ontology, owl2RLPersistenceLayer);
        }

        return swrlBridge;
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
     * @return A WSWRL-based wrapper of an OWL ontology
     * @throws SWRLBuiltInException If a SQWRL error occurs during ontology
     *                              processing
     */
    public static WSWRLOntology createWSWRLAPIOntology(OWLOntology ontology) throws SWRLBuiltInException {
        WSWRLOntology wswrlOntology = new DefaultWSWRLOntology(ontology, WSWRLFactory.createIRIResolver());
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
        WSWRLBuiltinInvoker builtinInvoker = DefaultWSWRLBuiltinInvoker.getInstance(getBridge(ontology));
        return builtinInvoker;
    }
}
