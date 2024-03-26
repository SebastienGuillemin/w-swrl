package com.sebastienguillemin.wswrl.factory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.util.OWLAPIPreconditions;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.factory.DefaultSWRLAPIOWLDataFactory;

import com.sebastienguillemin.wswrl.core.engine.WSWRLBuiltinInvoker;
import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDifferentIndividualsAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLSameIndividualsAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLLiteralBuiltInVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.rule.DefaultWSWRLRule;
import com.sebastienguillemin.wswrl.rule.atom.builtin.DefaultWSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.rule.atom.property.DefaultWSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.rule.atom.property.DefaultWSWRLDifferentIndividualsAtom;
import com.sebastienguillemin.wswrl.rule.atom.property.DefaultWSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.rule.atom.property.DefaultWSWRLSameIndividuals;
import com.sebastienguillemin.wswrl.rule.atom.unary.DefaultWSWRLClassAtom;
import com.sebastienguillemin.wswrl.rule.variable.DefaultWSWRLDVariable;
import com.sebastienguillemin.wswrl.rule.variable.DefaultWSWRLIVariable;
import com.sebastienguillemin.wswrl.rule.variable.DefaultWSWRLLiteralBuiltinVariable;

import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

/**
 * {@inheritDoc}
 */
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
    public WSWRLRule getWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean enabled)
            throws MissingRankException {
        return new DefaultWSWRLRule(ruleName, head, body, enabled);
    }

    @Override
    public WSWRLClassAtom getWSWRLClassAtom(OWLClass atomClass, WSWRLIVariable iVariable) {
        OWLAPIPreconditions.checkNotNull(atomClass, CLASS_PREDICATE_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(iVariable, ARG0_CANNOT_BE_NULL);
        return new DefaultWSWRLClassAtom(atomClass, iVariable, null);
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

    @Override
    public WSWRLDifferentIndividualsAtom getWSWRLDifferentIndividualsAtom(@NonNull WSWRLIVariable subject, @NonNull WSWRLIVariable object) {
        OWLAPIPreconditions.checkNotNull(subject, ARG0_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(object, ARG1_CANNOT_BE_NULL);
        
        return new DefaultWSWRLDifferentIndividualsAtom(new OWLObjectPropertyImpl(IRI.create("http://www.w3.org/2002/07/owl#differentFrom")), subject, object);
    }

    @Override
    public WSWRLSameIndividualsAtom getWSWRLSameIndividualsAtom(@NonNull WSWRLIVariable subject, @NonNull WSWRLIVariable object) {
        OWLAPIPreconditions.checkNotNull(subject, ARG0_CANNOT_BE_NULL);
        OWLAPIPreconditions.checkNotNull(object, ARG1_CANNOT_BE_NULL);
        
        return new DefaultWSWRLSameIndividuals(new OWLObjectPropertyImpl(IRI.create("http://www.w3.org/2002/07/owl#sameAs")), subject, object);
    }

    @Override
    public WSWRLBuiltInAtom getWSWRLBuiltInAtom(String ruleName, IRI builtInIRI, String builtInPrefixedName, List<WSWRLDVariable> variables, WSWRLOntology ontology) {
        List<WSWRLLiteralBuiltInVariable> wswrlLiteralBuiltInVariables = new ArrayList<>();

        for (WSWRLDVariable variable : variables) {
            wswrlLiteralBuiltInVariables.add(new DefaultWSWRLLiteralBuiltinVariable(variable));
        }

        WSWRLBuiltinInvoker builtinInvoker = WSWRLInternalFactory.getWSWRLlBuiltinInvoker(ontology);

        return new DefaultWSWRLBuiltInAtom(ruleName, builtInIRI, builtInPrefixedName, wswrlLiteralBuiltInVariables, builtinInvoker);
    }
}
