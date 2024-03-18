package com.sebastienguillemin.wswrl.core.factory;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.bridge.TargetSWRLRuleEngine;
import org.swrlapi.bridge.TargetSWRLRuleEngineCreator;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.InvalidSWRLRuleEngineNameException;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.exceptions.SWRLRuleEngineException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.factory.SWRLAPIInternalFactory;
import org.swrlapi.owl2rl.OWL2RLPersistenceLayer;

import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.engine.DefaultWSWRLRuleEngine;

public class WSWRLFactory extends SWRLAPIFactory {

    /**
     * @param ontology An OWL ontology
     * @return A SWRL rule engine
     * @throws SWRLBuiltInException 
     * @throws SWRLRuleEngineException If an error occurs during rule engine
     *                                 creation
     */
    public static WSWRLRuleEngine createWSWRLRuleEngine(OWLOntology ontology) throws SWRLBuiltInException {
        IRIResolver iriResolver = createIRIResolver();
        WSWRLOntology wswrlOntology = WSWRLInternalFactory.createWSWRLAPIOntology(ontology, iriResolver);
        
        return new DefaultWSWRLRuleEngine(wswrlOntology);
    }
}
