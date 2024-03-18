package com.sebastienguillemin.wswrl.core;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;

public interface WSWRLRuleEngineFactory {
    public WSWRLRuleEngine createWSWRLRuleEngine(OWLOntology ontology, IRIResolver iriResolver)
            throws SWRLBuiltInException;

    public WSWRLRuleEngine createWSWRLRuleEngine(String ruleName, OWLOntology ontology, IRIResolver iriResolver)
            throws SWRLBuiltInException;

    public void registerRuleEngine(TargetWSWRLRuleEngineCreator ruleEngineCreator);
}
