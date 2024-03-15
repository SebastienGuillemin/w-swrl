package com.sebastienguillemin.wswrl.core.factory;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.swrlapi.factory.SWRLAPIOWLDataFactory;

import com.sebastienguillemin.wswrl.core.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.WSWRLVariable;

public interface WSWRLDataFactory extends SWRLAPIOWLDataFactory {
    WSWRLClassAtom getWSWRLClassAtom(OWLClass cls, @NonNull SWRLIArgument iArgument);  
    WSWRLVariable getWSWRLVariable(IRI iri);  
}
