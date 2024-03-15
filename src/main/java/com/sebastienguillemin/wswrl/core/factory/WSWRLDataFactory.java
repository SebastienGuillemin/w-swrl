package com.sebastienguillemin.wswrl.core.factory;

import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.swrlapi.factory.SWRLAPIOWLDataFactory;

import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.WSWRLVariable;

public interface WSWRLDataFactory extends SWRLAPIOWLDataFactory {
    public WSWRLClassAtom getWSWRLClassAtom(OWLClass cls, @NonNull SWRLIArgument iArgument);  
    public WSWRLVariable getWSWRLVariable(IRI iri);
    public WSWRLRule getWSWRLRule(Set<WSWRLAtom> body, Set<WSWRLAtom> head, boolean enabled);
}
