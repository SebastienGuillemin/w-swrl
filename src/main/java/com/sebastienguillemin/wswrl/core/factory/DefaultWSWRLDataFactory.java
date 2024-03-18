package com.sebastienguillemin.wswrl.core.factory;

import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.util.OWLAPIPreconditions;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.factory.DefaultSWRLAPIOWLDataFactory;

import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.DefaultWSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.DefaultWSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.atom.binary.DefaultWSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.unary.DefaultWSWRLClassAtom;

public class DefaultWSWRLDataFactory extends DefaultSWRLAPIOWLDataFactory implements WSWRLDataFactory {
    private static final String ARG0_CANNOT_BE_NULL = "arg0 cannot be null";
    private static final String ARG1_CANNOT_BE_NULL = "arg1 cannot be null";
    private static final String PROPERTY_CANNOT_BE_NULL = "property cannot be null";
    private static final String CLASS_PREDICATE_CANNOT_BE_NULL = "class predicate cannot be null";
    private static final String VARIABLE_CANNOT_BE_NULL = "var cannot be null";

    public DefaultWSWRLDataFactory(@NonNull IRIResolver iriResolver) {
        super(iriResolver);
    }

    @Override
    public WSWRLRule getWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean enabled) {
        return new DefaultWSWRLRule(ruleName, head, body, enabled);
    }

    @Override
    public WSWRLClassAtom getWSWRLClassAtom(OWLClass predicate, @NonNull SWRLIArgument iArgument) {
        OWLAPIPreconditions.checkNotNull(predicate, CLASS_PREDICATE_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(iArgument, ARG0_CANNOT_BE_NULL);
        return new DefaultWSWRLClassAtom(predicate, iArgument, null);
    }

    @Override
    public WSWRLVariable getWSWRLVariable(IRI iri) {
        OWLAPIPreconditions.checkNotNull(iri, VARIABLE_CANNOT_BE_NULL);
        return new DefaultWSWRLVariable(iri);
    }

    @Override
    public WSWRLObjectPropertyAtom getWSWRLObjectPropertyAtom(OWLObjectProperty objectProperty,
            SWRLIArgument swrliArgument, SWRLIArgument swrliArgument2) {
        OWLAPIPreconditions.checkNotNull(objectProperty, PROPERTY_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(swrliArgument, ARG0_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(swrliArgument2, ARG1_CANNOT_BE_NULL);

        return new DefaultWSWRLObjectPropertyAtom(objectProperty, swrliArgument, swrliArgument2);
    }

}
