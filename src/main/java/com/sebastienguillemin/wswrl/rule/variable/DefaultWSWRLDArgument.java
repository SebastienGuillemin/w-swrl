package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.OWLLiteral;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDArgument;

import uk.ac.manchester.cs.owl.owlapi.SWRLLiteralArgumentImpl;

public class DefaultWSWRLDArgument extends SWRLLiteralArgumentImpl implements WSWRLDArgument {

    public DefaultWSWRLDArgument(OWLLiteral literal) {
        super(literal);
    }
}
