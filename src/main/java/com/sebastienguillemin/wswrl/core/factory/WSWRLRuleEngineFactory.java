package com.sebastienguillemin.wswrl.core.factory;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.bridge.TargetSWRLRuleEngineCreator;
import org.swrlapi.core.IRIResolver;

import com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.exception.WSWRLRuleEngineException;

/**
 * Factory that creates
 * {@link com.sebastienguillemin.wswrl.core.engine.WSWRLRuleEngine} instances.
 */
public interface WSWRLRuleEngineFactory {
    /**
     * Creates new WSWRL rule engine. Relies on
     * {@link com.sebastienguillemin.wswrl.core.factory.WSWRLRuleEngineFactory#createWSWRLRuleEngine(String, String, OWLOntology, IRIResolver, OWLOntologyManager)}.
     * This methos find automatically available SWRL and WSWRL target rule
     * engines.
     * 
     * @see com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine
     * @see org.swrlapi.bridge.TargetSWRLRuleEngine
     * 
     * @param ontology    The ontology for which the engine is created.
     * @param iriResolver The IRI resolver used by the rule engine.
     * @param ontologyManager An ontology manager used by a WSWRL target rule engine to save inferred axioms.
     * @return A new rule engine instance.
     * @throws WSWRLRuleEngineException If an error occurs.
     */
    public WSWRLRuleEngine createWSWRLRuleEngine(OWLOntology ontology, IRIResolver iriResolver, OWLOntologyManager ontologyManager)
            throws WSWRLRuleEngineException;

    /**
     * 
     * @param swrlRuleEngineName  The target SWRL rule engone name.
     * @param wswrlRuleEngineName The target WSWRL rule engine name.
     * @param ontology            The ontology for which the engine is created.
     * @param iriResolver         The IRI resolver used by the rule engine.
     * @param ontologyManager An ontology manager used by a WSWRL target rule engine to save inferred axioms.
     * @return A new rule engine instance.
     * @throws WSWRLRuleEngineException If an error occurs.
     */
    public WSWRLRuleEngine createWSWRLRuleEngine(String swrlRuleEngineName, String wswrlRuleEngineName,
            OWLOntology ontology, IRIResolver iriResolver, OWLOntologyManager ontologyManager) throws WSWRLRuleEngineException;

    /**
     * Registers a
     * {@link com.sebastienguillemin.wswrl.core.factory.TargetWSWRLRuleEngineCreator}used
     * while creating a wswrl rule engine.
     * 
     * @param wswrlRuleEngineCreator The WSWRL rule engine create to register.
     */
    public void registerWSWRLRuleEngine(TargetWSWRLRuleEngineCreator wswrlRuleEngineCreator);

    /**
     * Registers a {@link org.swrlapi.bridge.TargetSWRLRuleEngineCreator} used while
     * creating a wswrl rule engine.
     * 
     * @param swrlRuleEngineCreator The SWRL rule engine create to register.
     */
    public void registerSWRLRuleEngine(TargetSWRLRuleEngineCreator swrlRuleEngineCreator);
}
