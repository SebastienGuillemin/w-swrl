package com.sebastienguillemin.wswrl.rule.variable;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIArgument;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;

import uk.ac.manchester.cs.owl.owlapi.SWRLIndividualArgumentImpl;

public class DefaultWSWRLIArgument extends SWRLIndividualArgumentImpl implements WSWRLIArgument {
    private final WSWRLIndividual wswrlIndividual;

    public DefaultWSWRLIArgument(OWLNamedIndividual individual) {
        super(individual);
        this.wswrlIndividual = new DefaultWSWRLIndividual(individual);
    }

    @Override
    public WSWRLIndividual getWSWRLIndividual() {
        return this.wswrlIndividual;
    }
    
}
