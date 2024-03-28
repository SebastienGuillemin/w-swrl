package com.sebastienguillemin.wswrl.factory;

import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.bridge.TargetSWRLRuleEngine;
import org.swrlapi.bridge.TargetSWRLRuleEngineCreator;
import org.swrlapi.core.SWRLRuleEngineManager;
import org.swrlapi.drools.core.DroolsSWRLRuleEngineCreator;
import org.swrlapi.exceptions.NoRegisteredSWRLRuleEnginesException;

import com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngineManager;
import com.sebastienguillemin.wswrl.core.factory.TargetWSWRLRuleEngineCreator;
import com.sebastienguillemin.wswrl.core.factory.WSWRLRuleEngineFactory;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.engine.DefaultWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.exception.NoRegisteredWSWRLRuleEnginesException;
import com.sebastienguillemin.wswrl.exception.WSWRLRuleEngineException;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLRuleEngineFactory implements WSWRLRuleEngineFactory {
    private SWRLRuleEngineManager swrlRuleEngineManager;
    private WSWRLRuleEngineManager wswrlRuleEngineManager;

    /**
     * Default constructor.
     */
    public DefaultWSWRLRuleEngineFactory() {
        this.swrlRuleEngineManager = WSWRLInternalFactory.createSWRLRuleEngineManager();
        this.registerSWRLRuleEngine(new DroolsSWRLRuleEngineCreator());

        this.wswrlRuleEngineManager = WSWRLInternalFactory.createWSWRLRuleEngineManager();
        this.registerWSWRLRuleEngine(new DefaultTargetWSWRLRuleEngineCreator());
    }

    @Override
    public void registerWSWRLRuleEngine(TargetWSWRLRuleEngineCreator ruleEngineCreator) {
        this.wswrlRuleEngineManager.registerRuleEngine(ruleEngineCreator);
    }

    @Override
    public void registerSWRLRuleEngine(TargetSWRLRuleEngineCreator ruleEngineCreator) {
        this.swrlRuleEngineManager.registerRuleEngine(ruleEngineCreator);
    }

    @Override
    public WSWRLRuleEngine createWSWRLRuleEngine(WSWRLOntology ontology) throws WSWRLRuleEngineException {

        String swrlRuleEngineName, wswrlRuleEngineName;

        if (this.swrlRuleEngineManager.hasRegisteredRuleEngines()) {
            swrlRuleEngineName = this.swrlRuleEngineManager.getAnyRegisteredRuleEngineName().get();

        } else
            throw new NoRegisteredSWRLRuleEnginesException();

        if (this.wswrlRuleEngineManager.hasRegisteredRuleEngines()) {
            wswrlRuleEngineName = this.wswrlRuleEngineManager.getAnyRegisteredRuleEngineName();

        } else
            throw new NoRegisteredWSWRLRuleEnginesException();

        if (swrlRuleEngineName != null && wswrlRuleEngineName != null)
            return createWSWRLRuleEngine(swrlRuleEngineName, wswrlRuleEngineName, ontology);
        else
            throw new NoRegisteredWSWRLRuleEnginesException();
    }

    @Override
    public WSWRLRuleEngine createWSWRLRuleEngine(String swrlRuleEngineName, String wswrlRuleEngineName, WSWRLOntology ontology) throws WSWRLRuleEngineException {
        try {
            SWRLBridge bridge = WSWRLInternalFactory.getBridge(ontology);

            TargetSWRLRuleEngineCreator targetSWRLRuleEngineCreator = this.swrlRuleEngineManager
                    .getRegisteredRuleEngineCreator(swrlRuleEngineName).get();
            TargetWSWRLRuleEngineCreator targetWSWRLRuleEngineCreator = this.wswrlRuleEngineManager
                    .getRegisteredRuleEngineCreator(wswrlRuleEngineName);

            if (targetSWRLRuleEngineCreator != null && targetWSWRLRuleEngineCreator != null) {

                TargetSWRLRuleEngine targetSWRLRuleEngine = targetSWRLRuleEngineCreator.create(bridge);
                bridge.setTargetSWRLRuleEngine(targetSWRLRuleEngine);

                TargetWSWRLRuleEngine targetWSWRLRuleEngine = targetWSWRLRuleEngineCreator.create(ontology);

                WSWRLRuleEngine ruleEngine = new DefaultWSWRLRuleEngine(ontology, targetWSWRLRuleEngine,
                        targetSWRLRuleEngine, bridge, bridge);
                ruleEngine.importAssertedOWLAxioms();

                return ruleEngine;
            } else
                throw new WSWRLRuleEngineException("Error creating wswrl rule engine " + wswrlRuleEngineName
                        + " swrl rule engine " + swrlRuleEngineName + ". Creator failed.");
        } catch (Throwable e) {
            throw new WSWRLRuleEngineException("Error creating rule engine " + wswrlRuleEngineName + ". Exception: "
                    + e.getClass().getCanonicalName() + ". Message: " + (e.getMessage() != null ? e.getMessage() : ""),
                    e);
        }
    }

}
