package com.sebastienguillemin.wswrl.core.factory.imp;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.factory.DefaultSWRLAPIOWLDataFactory;

import com.sebastienguillemin.wswrl.core.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.rule.DefaultWSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.atom.unary.DefaultWSWRLClassAtom;

public class DefaultWSWRLDataFactory extends DefaultSWRLAPIOWLDataFactory implements WSWRLDataFactory {

    public DefaultWSWRLDataFactory(@NonNull IRIResolver iriResolver) {
        super(iriResolver);
    }

    @Override
    public WSWRLClassAtom getWSWRLClassAtom(OWLClass cls, @NonNull SWRLIArgument iArgument) {
        System.out.println("Creating WSWRLClassAtom: " + cls + " " +  iArgument);
        return new DefaultWSWRLClassAtom(cls, iArgument, null);
    }

    @Override
    public WSWRLVariable getWSWRLVariable(IRI iri) {
        return new DefaultWSWRLVariable(iri);
    }
    
}
