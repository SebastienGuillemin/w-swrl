package com.sebastienguillemin.wswrl.core.ontology;

import java.util.Set;

import org.swrlapi.core.SWRLAPIOWLOntology;

import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.exception.WSWRLParseException;

public interface WSWRLOntology extends SWRLAPIOWLOntology {

    public WSWRLRule createWSWRLRule(String ruleName, String rule)
            throws WSWRLParseException, WSWRLBuiltInException, MissingRankException;

    public WSWRLRule createWSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, WSWRLBuiltInException, MissingRankException;

    public Set<WSWRLRule> getWSWRLRules();

    public WSWRLDataFactory getWSWRLDataFactory();

}
