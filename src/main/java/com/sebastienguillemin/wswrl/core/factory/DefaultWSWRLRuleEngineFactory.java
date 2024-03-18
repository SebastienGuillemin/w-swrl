package com.sebastienguillemin.wswrl.core.factory;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.bridge.TargetSWRLRuleEngine;
import org.swrlapi.bridge.TargetSWRLRuleEngineCreator;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.exceptions.SWRLRuleEngineException;
import org.swrlapi.factory.SWRLAPIInternalFactory;
import org.swrlapi.owl2rl.OWL2RLPersistenceLayer;

import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngineCreator;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngineFactory;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngineManager;
import com.sebastienguillemin.wswrl.core.engine.DefaultWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.engine.target.DefaultTargetWSWRLRuleEngineCreator;
import com.sebastienguillemin.wswrl.core.exception.NoRegisteredWSWRLRuleEnginesException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLRuleEngineException;

public class DefaultWSWRLRuleEngineFactory implements WSWRLRuleEngineFactory {
    private WSWRLRuleEngineManager ruleEngineManager;

    public DefaultWSWRLRuleEngineFactory() {
        this.ruleEngineManager = WSWRLInternalFactory.createWSWRLRuleEngineManager();
        this.registerRuleEngine(new DefaultTargetWSWRLRuleEngineCreator());
    }

    @Override
    public void registerRuleEngine(TargetWSWRLRuleEngineCreator ruleEngineCreator) {
        this.ruleEngineManager.registerRuleEngine(ruleEngineCreator);
    }

    @Override
    public WSWRLRuleEngine createWSWRLRuleEngine(OWLOntology ontology, IRIResolver iriResolver)
            throws SWRLBuiltInException {
        if (this.ruleEngineManager.hasRegisteredRuleEngines()) {
            String ruleEngineName = this.ruleEngineManager.getAnyRegisteredRuleEngineName();
            if (ruleEngineName != null)
                return createWSWRLRuleEngine(ruleEngineName, ontology, iriResolver);
            else
                throw new NoRegisteredWSWRLRuleEnginesException();
        } else
            throw new NoRegisteredWSWRLRuleEnginesException();
    }

    @Override
    public WSWRLRuleEngine createWSWRLRuleEngine(String ruleEngineName, OWLOntology OWLOntology,
            IRIResolver iriResolver) throws WSWRLBuiltInException {
        try {
            WSWRLOntology WSWRLOntology = WSWRLInternalFactory.createWSWRLAPIOntology(OWLOntology, iriResolver);

            OWL2RLPersistenceLayer owl2RLPersistenceLayer = WSWRLInternalFactory.createOWL2RLPersistenceLayer(OWLOntology);

            SWRLBridge bridge = SWRLAPIInternalFactory.createSWRLBridge(WSWRLOntology, owl2RLPersistenceLayer);
            TargetWSWRLRuleEngineCreator targetSWRLRuleEngineCreator = this.ruleEngineManager.getRegisteredRuleEngineCreator(ruleEngineName);

            if (targetSWRLRuleEngineCreator != null) {
                TargetSWRLRuleEngine targetSWRLRuleEngine = targetSWRLRuleEngineCreator.create(bridge, WSWRLOntology);
                bridge.setTargetSWRLRuleEngine(targetSWRLRuleEngine);

                WSWRLRuleEngine ruleEngine = new DefaultWSWRLRuleEngine(WSWRLOntology, targetSWRLRuleEngine, bridge,
                        bridge);
                ruleEngine.importAssertedOWLAxioms();

                return ruleEngine;
            } else
                throw new WSWRLRuleEngineException (
                        "Error creating rule engine " + ruleEngineName + ". Creator failed.");
        } catch (Throwable e) {
            throw new WSWRLRuleEngineException("Error creating rule engine " + ruleEngineName + ". Exception: "
                    + e.getClass().getCanonicalName() + ". Message: " + (e.getMessage() != null ? e.getMessage() : ""),
                    e);
        }
    }

}
