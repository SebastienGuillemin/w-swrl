package com.sebastienguillemin.wswrl.core;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.swrlapi.factory.SWRLAPIOWLDataFactory;

public interface WSWRLDataFactory extends SWRLAPIOWLDataFactory {

    public WSWRLVariable getWSWRLVariable(IRI iri);

    public WSWRLRule getWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean enabled);

    public WSWRLClassAtom getWSWRLClassAtom(OWLClass cls, SWRLIArgument iArgument);

    public WSWRLObjectPropertyAtom getWSWRLObjectPropertyAtom(OWLObjectProperty objectProperty,
            SWRLIArgument swrliArgument, SWRLIArgument swrliArgument2);
}
