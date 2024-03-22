package com.sebastienguillemin.wswrl.core.factory;

import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.swrlapi.factory.SWRLAPIOWLDataFactory;

import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.exception.MissingRankException;

public interface WSWRLDataFactory extends SWRLAPIOWLDataFactory {

    public WSWRLVariable getWSWRLVariable(IRI iri, WSWRLVariableDomain domain);

    public WSWRLRule getWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean enabled) throws MissingRankException;

    public WSWRLClassAtom getWSWRLClassAtom(OWLClass cls, WSWRLIVariable iArgument);

    public WSWRLObjectPropertyAtom getWSWRLObjectPropertyAtom(OWLObjectProperty objectProperty,
            WSWRLIVariable swrliArgument, WSWRLIVariable swrliArgument2);

    public WSWRLDataPropertyAtom getWSWRLDataPropertyAtom(OWLDataProperty dataProperty, @NonNull WSWRLIVariable subject,
            @NonNull WSWRLDVariable object);
}
