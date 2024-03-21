package com.sebastienguillemin.wswrl.core.factory;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.swrlapi.factory.SWRLAPIOWLDataFactory;

import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIArgument;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.exception.MissingRankException;

public interface WSWRLDataFactory extends SWRLAPIOWLDataFactory {

    public WSWRLVariable getWSWRLVariable(IRI iri, WSWRLVariableDomain domain);

    public WSWRLRule getWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean enabled) throws MissingRankException;

    public WSWRLClassAtom getWSWRLClassAtom(OWLClass cls, WSWRLIArgument iArgument);

    public WSWRLObjectPropertyAtom getWSWRLObjectPropertyAtom(OWLObjectProperty objectProperty,
            WSWRLIArgument swrliArgument, WSWRLIArgument swrliArgument2);
}
