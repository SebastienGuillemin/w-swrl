package com.sebastienguillemin.wswrl.core.factory;

import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.swrlapi.factory.SWRLAPIOWLDataFactory;

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
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.exception.MissingRankException;

/**
 * Factory that creates data class instances (eg., atoms, rule etc.)
 */
public interface WSWRLDataFactory extends SWRLAPIOWLDataFactory {

    /**
     * Returns the instance of a variable that corresponds to an IRI. If no variable
     * exists for that IRI, a new instance will be created. Otherwise it returns the
     * instance.
     * 
     * @param iri    Variable IRI.
     * @param domain Variable domain.
     * @return The variable instance.
     */
    public WSWRLVariable getWSWRLVariable(IRI iri, WSWRLVariableDomain domain);

    /**
     * 
     * @param ruleName The rule name.
     * @param head     List of the head
     *                 {@link com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom}.
     * @param body     List of the body
     *                 {@link com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom}.
     * @param enabled  True if the rule is enabled, False otherwise.
     * @return A new {@link com.sebastienguillemin.wswrl.core.rule.WSWRLRule}.
     * @throws MissingRankException
     */
    public WSWRLRule getWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean enabled)
            throws MissingRankException;

    /**
     * 
     * @param atomClass The atom class.
     * @param iVariable The atom variable.
     * @return A new wswrl class atom.
     */
    public WSWRLClassAtom getWSWRLClassAtom(OWLClass atomClass, WSWRLIVariable iVariable);

    /**
     * 
     * @param objectProperty The OWL object property.
     * @param subject        The property subject (a variable).
     * @param object         The property object (a variable).
     * @return A new wswrl object property atom.
     */
    public WSWRLObjectPropertyAtom getWSWRLObjectPropertyAtom(OWLObjectProperty objectProperty,
            WSWRLIVariable subject, WSWRLIVariable object);

    /**
     * 
     * @param dataProperty The OWL data property.
     * @param subject      The property subject (a variable).
     * @param object       The property object (a variable).
     * @return A new wswrl data property atom.
     */
    public WSWRLDataPropertyAtom getWSWRLDataPropertyAtom(OWLDataProperty dataProperty, WSWRLIVariable subject,
            WSWRLDVariable object);

    /**
     * 
     * @param subject The atom subject (a variable).
     * @param object  The atom object (a variable).
     * @return A new wswrl different individuals atom (i.e, differentFrom).
     */
    public WSWRLDifferentIndividualsAtom getWSWRLDifferentIndividualsAtom(@NonNull WSWRLIVariable subject,
            WSWRLIVariable object);

    /**
     * 
     * @param subject The atom subject (a variable).
     * @param object  The atom object (a variable).
     * @return A new wswrl same individuals atom (i.e, sameAs).
     */
    public WSWRLSameIndividualsAtom getWSWRLSameIndividualsAtom(@NonNull WSWRLIVariable subject, WSWRLIVariable object);

    /**
     * 
     * @param ruleName            The built-in rule name.
     * @param builtInIRI          The built-in IRI.
     * @param builtInPrefixedName The built-in prefixed name (e.g., swrlb:contains).
     * @param variables           The built-in variables.
     * @param ontology            The ontology (used here to create a
     *                            {@link com.sebastienguillemin.wswrl.core.engine.WSWRLBuiltinInvoker}).
     * @return A new wswrl built-in atom.
     */
    public WSWRLBuiltInAtom getWSWRLBuiltInAtom(String ruleName, IRI builtInIRI, String builtInPrefixedName,
            List<WSWRLDVariable> variables, WSWRLOntology ontology);
}
