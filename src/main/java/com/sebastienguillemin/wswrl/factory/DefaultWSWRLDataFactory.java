package com.sebastienguillemin.wswrl.factory;

import java.util.Hashtable;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.util.OWLAPIPreconditions;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.factory.DefaultSWRLAPIOWLDataFactory;

import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
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
import com.sebastienguillemin.wswrl.rule.DefaultWSWRLRule;
import com.sebastienguillemin.wswrl.rule.atom.DefaultWSWRLClassAtom;
import com.sebastienguillemin.wswrl.rule.atom.DefaultWSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.rule.atom.DefaultWSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.rule.variable.DefaultWSWRLDVariable;
import com.sebastienguillemin.wswrl.rule.variable.DefaultWSWRLIVariable;

public class DefaultWSWRLDataFactory extends DefaultSWRLAPIOWLDataFactory implements WSWRLDataFactory {
    private static final String ARG0_CANNOT_BE_NULL = "arg0 cannot be null";
    private static final String ARG1_CANNOT_BE_NULL = "arg1 cannot be null";
    private static final String PROPERTY_CANNOT_BE_NULL = "property cannot be null";
    private static final String CLASS_PREDICATE_CANNOT_BE_NULL = "class predicate cannot be null";
    private static final String VARIABLE_CANNOT_BE_NULL = "var cannot be null";

    private Hashtable<IRI, WSWRLVariable> variables;

    public DefaultWSWRLDataFactory(@NonNull IRIResolver iriResolver) {
        super(iriResolver);

        this.variables = new Hashtable<>();
    }

    @Override
    public WSWRLRule getWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean enabled)
            throws MissingRankException {
        return new DefaultWSWRLRule(ruleName, head, body, enabled);
    }

    @Override
    public WSWRLClassAtom getWSWRLClassAtom(OWLClass predicate, @NonNull WSWRLIVariable object) {
        OWLAPIPreconditions.checkNotNull(predicate, CLASS_PREDICATE_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(object, ARG0_CANNOT_BE_NULL);
        return new DefaultWSWRLClassAtom(predicate, object, null);
    }

    public WSWRLVariable getWSWRLVariable(IRI iri, WSWRLVariableDomain domain) {
        OWLAPIPreconditions.checkNotNull(iri, VARIABLE_CANNOT_BE_NULL);

        WSWRLVariable variable = null;
        if (!this.variables.containsKey(iri)) {
            if (domain == WSWRLVariableDomain.INDIVIDUALS)
                variable = new DefaultWSWRLIVariable(iri);
            if (domain == WSWRLVariableDomain.DATA)
                variable = new DefaultWSWRLDVariable(iri);
            this.variables.put(iri, variable);
        } else
            variable = this.variables.get(iri);

        return variable;
    }

    @Override
    public WSWRLObjectPropertyAtom getWSWRLObjectPropertyAtom(OWLObjectProperty objectProperty, WSWRLIVariable subject,
            WSWRLIVariable object) {
        OWLAPIPreconditions.checkNotNull(objectProperty, PROPERTY_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(subject, ARG0_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(object, ARG1_CANNOT_BE_NULL);

        return new DefaultWSWRLObjectPropertyAtom(objectProperty, subject, object);
    }

    @Override
    public WSWRLDataPropertyAtom getWSWRLDataPropertyAtom(OWLDataProperty dataProperty, WSWRLIVariable subject,
            WSWRLDVariable object) {
        OWLAPIPreconditions.checkNotNull(dataProperty, PROPERTY_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(subject, ARG0_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(object, ARG1_CANNOT_BE_NULL);

        return new DefaultWSWRLDataPropertyAtom(dataProperty, subject, object);
    }
}
