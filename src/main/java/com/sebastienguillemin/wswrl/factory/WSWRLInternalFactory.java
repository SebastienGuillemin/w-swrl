package com.sebastienguillemin.wswrl.factory;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.DefaultSWRLBridge;
import org.swrlapi.factory.SWRLAPIInternalFactory;
import org.swrlapi.owl2rl.OWL2RLPersistenceLayer;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngineManager;
import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.factory.WSWRLRuleEngineFactory;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.ontology.DefaultWSWRLOntology;
import com.sebastienguillemin.wswrl.rule.atom.builtin.WSWRLBuiltinInvoker;

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
     * Create a {@link com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology} from an
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

    public static WSWRLBuiltinInvoker getWSWRLlBuiltinInvoker(WSWRLOntology ontology, OWL2RLPersistenceLayer persistenceLayer) {
        SWRLBridge bridge = new DefaultSWRLBridge(ontology, persistenceLayer);
        WSWRLBuiltinInvoker builtinInvoker = new WSWRLBuiltinInvoker(bridge);
        return builtinInvoker;
    }
}
