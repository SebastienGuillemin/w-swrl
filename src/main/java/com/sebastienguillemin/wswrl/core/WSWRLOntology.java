package com.sebastienguillemin.wswrl.core;

import java.util.Set;

import org.swrlapi.core.SWRLAPIOWLOntology;

import com.sebastienguillemin.wswrl.exception.AlreadyInRankException;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.exception.WSWRLParseException;

public interface WSWRLOntology extends SWRLAPIOWLOntology {

    public WSWRLRule createWSWRLRule(String ruleName, String rule)
            throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException, MissingRankException;

    public WSWRLRule createWSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException, MissingRankException;

    public Set<WSWRLRule> getWSWRLRules();

    public WSWRLDataFactory getWSWRLDataFactory();

}
