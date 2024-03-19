package com.sebastienguillemin.wswrl.core;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.bridge.TargetSWRLRuleEngineCreator;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;

import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;

public interface WSWRLRuleEngineFactory {
    public WSWRLRuleEngine createWSWRLRuleEngine(OWLOntology ontology, IRIResolver iriResolver)
            throws SWRLBuiltInException;

    public WSWRLRuleEngine createWSWRLRuleEngine(String swrlRuleEngineName, String wswrlRuleEngineName,
            OWLOntology OWLOntology, IRIResolver iriResolver) throws WSWRLBuiltInException;

    public void registerWSWRLRuleEngine(TargetWSWRLRuleEngineCreator wswrlRuleEngineCreator);

    public void registerSWRLRuleEngine(TargetSWRLRuleEngineCreator swrlRuleEngineCreator);
}
