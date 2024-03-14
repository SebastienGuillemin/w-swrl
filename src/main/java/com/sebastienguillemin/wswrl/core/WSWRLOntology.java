package com.sebastienguillemin.wswrl.core;

import org.swrlapi.core.SWRLAPIOWLOntology;

import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;

public interface WSWRLOntology extends SWRLAPIOWLOntology {

    public WSWRLRule createWSWRLRule(String ruleName, String rule) throws WSWRLParseException, WSWRLBuiltInException;

    public WSWRLRule createWSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, WSWRLBuiltInException;

}
