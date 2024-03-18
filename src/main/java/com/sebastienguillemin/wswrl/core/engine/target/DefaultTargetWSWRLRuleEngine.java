package com.sebastienguillemin.wswrl.core.engine.target;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLPredicate;
import org.swrlapi.bridge.SWRLBridge;
import org.swrlapi.drools.core.DroolsSWRLRuleEngine;

import com.sebastienguillemin.wswrl.core.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;

public class DefaultTargetWSWRLRuleEngine extends DroolsSWRLRuleEngine implements TargetWSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;

    public DefaultTargetWSWRLRuleEngine(SWRLBridge bridge, WSWRLOntology WSWRLOntology) {
        super(bridge);
        this.wswrlOntology = WSWRLOntology;
    }

    @Override
    public void runRuleEngine() {
        Set<WSWRLRule> wswrlRules = this.wswrlOntology.getWSWRLRules();

        for (WSWRLAtom atom : ((WSWRLRule) wswrlRules.toArray()[0]).getBody()) {
            SWRLPredicate predicate = atom.getPredicate();
            
            if (atom instanceof SWRLClassAtom) {
                System.out.println(predicate + " is class atom");
            }
        }

        for (OWLAxiom axiom : this.wswrlOntology.getOWLAxioms()) {
            System.out.println(axiom);
        }
    }
}
