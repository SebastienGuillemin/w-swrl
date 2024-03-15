package com.sebastienguillemin.wswrl.core;

import org.swrlapi.core.SWRLAPIOWLOntology;

import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;

public interface WSWRLOntology extends SWRLAPIOWLOntology {

    public WSWRLRule createWSWRLRule(String ruleName, String rule) throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException;

    public WSWRLRule createWSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException;

    public WSWRLDataFactory getWSWRLDataFactory();

}
