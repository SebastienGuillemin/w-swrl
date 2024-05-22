package com.sebastienguillemin.wswrl.parser;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.XSDVocabulary;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.core.SWRLAPIOWLOntology;

import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataRangeAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDifferentIndividualsAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLSameIndividualsAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.rule.variable.DefaultWSWRLDVariable;

/**
 * Provides support methods used by the {@link com.sebastienguillemin.wswrl.parser.WSWRLParser}.
 * 
 * <br><br>
 * 
 * Code adapted from the SWRL API.
 *
 * @see com.sebastienguillemin.wswrl.parser.WSWRLParser
 */
public class WSWRLParserSupport {
    @NonNull
    private final WSWRLOntology wswrlOntology;

    public WSWRLParserSupport(WSWRLOntology wswrlOntology) {
        this.wswrlOntology = wswrlOntology;
    }

    public Set<WSWRLAtom> createWSWRLBodyAtomList() {
        return new LinkedHashSet<>();
    }

    public String getShortNameFromIRI(String value, boolean interactiveParseOnly) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getShortNameFromIRI'");
    }

    public WSWRLRule createWSWRLRule(String ruleName, WSWRLAtom head, Set<WSWRLAtom> body, boolean isEnabled)
            throws MissingRankException {
        // TODO : gérer le cas d'ajout d'une annotation.
        return this.getDataFactory().getWSWRLRule(ruleName, head, body, isEnabled);
    }

    public boolean isOWLEntity(String shortName) {
        return isOWLClass(shortName) || isOWLNamedIndividual(shortName) || isOWLObjectProperty(shortName)
                || isOWLDataProperty(shortName) || isOWLAnnotationProperty(shortName) || isOWLDatatype(shortName);
    }

    public boolean isOWLClass(String shortName) {
        IRI classIRI = prefixedName2IRI(shortName);

        return getOWLOntology().containsClassInSignature(classIRI, Imports.INCLUDED) || classIRI
                .equals(OWLRDFVocabulary.OWL_THING.getIRI()) || classIRI.equals(OWLRDFVocabulary.OWL_NOTHING.getIRI());
    }

    public boolean isWSWRLBuiltIn(String shortName) {
        return this.wswrlOntology.isSWRLBuiltIn(shortName);
    }

    public boolean isOWLObjectProperty(String shortName) {
        IRI propertyIRI = prefixedName2IRI(shortName);
        return this.getOWLOntology().containsObjectPropertyInSignature(propertyIRI, Imports.INCLUDED);
    }

    public boolean isOWLDataProperty(String shortName) {
        IRI propertyIRI = prefixedName2IRI(shortName);
        return this.getOWLOntology().containsDataPropertyInSignature(propertyIRI, Imports.INCLUDED);
    }

    public boolean isOWLDatatype(String shortName) {
        if (shortName.equals("rdf:PlainLiteral") || shortName.equals("rdfs:Literal")
                || shortName.equals("rdf:XMLLiteral"))
            return true;
        else
            try {
                XSDVocabulary.parseShortName(shortName);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
    }

    public WSWRLClassAtom createWSWRLClassAtom(String classShortName, WSWRLIVariable iArgument)
            throws WSWRLParseException {
        OWLClass cls = createOWLClass(classShortName);

        return this.getDataFactory().getWSWRLClassAtom(cls, iArgument);
    }

    public WSWRLObjectPropertyAtom createWSWRLObjectPropertyAtom(String propertyShortName,
            WSWRLIVariable subject,
            WSWRLIVariable object) throws WSWRLParseException {
        OWLObjectProperty objectProperty = this.createOWLObjectProperty(propertyShortName);

        return this.getDataFactory().getWSWRLObjectPropertyAtom(objectProperty, subject, object);
    }

    public WSWRLDataPropertyAtom createWSWRLDataPropertyAtom(String propertyShortName,
            WSWRLIVariable subject,
            WSWRLDVariable object) throws WSWRLParseException {
        // TODO Auto-generated method stub
        OWLDataProperty dataProperty = this.createOWLDataProperty(propertyShortName);

        return this.getDataFactory().getWSWRLDataPropertyAtom(dataProperty, subject, object);
    }

    public WSWRLBuiltInAtom createWSWRLBuiltInAtom(String ruleName, String builtInPrefixedName, List<WSWRLDVariable> arguments)
            throws WSWRLParseException {
        IRI builtInIRI = this.createSWRLBuiltInIRI(builtInPrefixedName);
        return this.getDataFactory().getWSWRLBuiltInAtom(ruleName, builtInIRI, builtInPrefixedName, arguments, this.wswrlOntology);
    }

    public WSWRLDataRangeAtom createWSWRLDataRangeAtom(String datatypePrefixedName,
            WSWRLDVariable WSWRLDVariable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLDataRangeAtom'");
    }

    public WSWRLSameIndividualsAtom createWSWRLSameIndividualAtom(WSWRLIVariable subject,
            WSWRLIVariable object) {
        return this.getDataFactory().getWSWRLSameIndividualsAtom(subject, object);
    }

    public WSWRLDifferentIndividualsAtom createWSWRLDifferentIndividualsAtom(WSWRLIVariable subject,
            WSWRLIVariable object) {
        // TODO Auto-generated method stub
        return this.getDataFactory().getWSWRLDifferentIndividualsAtom(subject, object);
    }

    public void checkThatWSWRLVariableNameIsValid(String variableName) throws WSWRLParseException {
        if (!isValidSWRLVariableName(variableName))
            throw new WSWRLParseException("Invalid WSWRL variable name " + variableName);

        if (isOWLEntity(variableName))
            throw new WSWRLParseException("Invalid WSWRL variable name " + variableName
                    + " - cannot use name of existing OWL class, individual, property, or datatype");
    }

    public WSWRLIVariable createWSWRLIVariable(String variableName) throws WSWRLParseException {
        if (isOWLEntity(variableName))
            throw new WSWRLParseException(variableName
                    + " cannot be used as a WSWRL variable name because it refers to an existing OWL entity");

        Optional<IRI> iri = getIRIResolver().variableName2IRI(variableName);

        if (iri.isPresent())
            return (WSWRLIVariable) this.getDataFactory().getWSWRLVariable(iri.get(), WSWRLVariableDomain.INDIVIDUALS);
        else
            throw new WSWRLParseException("error creating WSWRL variable " + variableName);
    }

    public WSWRLDVariable createWSWRLDVariable(String variableName) throws WSWRLParseException {
        if (isOWLEntity(variableName))
            throw new WSWRLParseException(variableName
                    + " cannot be used as a WSWRL variable name because it refers to an existing OWL entity");

        Optional<IRI> iri = getIRIResolver().variableName2IRI(variableName);

        if (iri.isPresent())
            return (WSWRLDVariable) this.getDataFactory().getWSWRLVariable(iri.get(), WSWRLVariableDomain.DATA);
        else
            throw new WSWRLParseException("error creating WSWRL variable " + variableName);
    }

    public boolean isOWLNamedIndividual(String shortName) {
        IRI individualIRI = prefixedName2IRI(shortName);
        return this.getOWLOntology().containsIndividualInSignature(individualIRI, Imports.INCLUDED);
    }

    public WSWRLIVariable createWSWRLIndividualArgument(String identifier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLIndividualArgument'");
    }

    public WSWRLDVariable createXSDIntegerSWRLLiteralArgument(String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createXSDIntegerSWRLLiteralArgument'");
    }

    public WSWRLDVariable createXSDDecimalSWRLLiteralArgument(String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createXSDDecimalSWRLLiteralArgument'");
    }

    public WSWRLDVariable createSWRLLiteralArgument(String literalValue, String datatype) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLLiteralArgument'");
    }

    @NonNull
    public WSWRLDVariable createXSDStringSWRLLiteralArgument(String lexicalValue) {
        OWLLiteral owlLiteral = this.getDataFactory().getOWLLiteralFactory().getOWLLiteral(lexicalValue);
        return new DefaultWSWRLDVariable(IRI.create("string constant"), owlLiteral);
    }

    public WSWRLDVariable createXSDBooleanSWRLLiteralArgument(String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createXSDBooleanSWRLLiteralArgument'");
    }

    public WSWRLDVariable createSWRLNamedIndividualBuiltInArgument(String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLNamedIndividualBuiltInArgument'");
    }

    public WSWRLDVariable createSWRLClassBuiltInArgument(String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLClassBuiltInArgument'");
    }

    public WSWRLDVariable createSWRLObjectPropertyBuiltInArgument(String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLObjectPropertyBuiltInArgument'");
    }

    public WSWRLDVariable createSWRLDataPropertyBuiltInArgument(String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLDataPropertyBuiltInArgument'");
    }

    public boolean isOWLAnnotationProperty(String shortName) {
        IRI propertyIRI = prefixedName2IRI(shortName);
        return getOWLOntology().containsAnnotationPropertyInSignature(propertyIRI, Imports.INCLUDED);
    }

    public WSWRLDVariable createSWRLAnnotationPropertyBuiltInArgument(String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLAnnotationPropertyBuiltInArgument'");
    }

    public WSWRLDVariable createSWRLDatatypeBuiltInArgument(String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLDatatypeBuiltInArgument'");
    }

    @NonNull
    private OWLClass createOWLClass(String classShortName) throws WSWRLParseException {
        if (isOWLClass(classShortName)) {
            IRI classIRI = prefixedName2IRI(classShortName);
            return this.getDataFactory().getOWLClass(classIRI);
        } else
            throw new WSWRLParseException(classShortName + " is not an OWL class");
    }

    @NonNull
    private OWLOntology getOWLOntology() {
        return this.wswrlOntology.getOWLOntology();
    }

    @NonNull
    private SWRLAPIOWLOntology getSWRLAPIOWLOntology() {
        return (SWRLAPIOWLOntology) this.wswrlOntology;
    }

    @NonNull
    private WSWRLDataFactory getDataFactory() {
        return this.wswrlOntology.getWSWRLDataFactory();
    }

    private IRI prefixedName2IRI(String prefixedName) {
        Optional<IRI> iri = getIRIResolver().prefixedName2IRI(prefixedName);

        if (iri.isPresent())
            return iri.get();
        else
            throw new IllegalArgumentException(
                    "could not loadExternalSWRLBuiltInLibraries IRI for prefixed name " + prefixedName);
    }

    /**
     * Check that a variable name is a valid SWRL variable. Somewhat arbitrary at
     * the moment.
     * We allow the same characters as a Java variable plus the ':' and '-'
     * characters.
     *
     * @param candidateSWRLVariableName The candidate variable name
     * @return True if variable name is valid, false otherwise
     */
    private boolean isValidSWRLVariableName(String candidateSWRLVariableName) {
        if (candidateSWRLVariableName.length() == 0)
            return false;

        if (!Character.isJavaIdentifierStart(candidateSWRLVariableName.charAt(0)))
            return false;

        for (int i = 1; i < candidateSWRLVariableName.length(); i++) {
            char c = candidateSWRLVariableName.charAt(i);
            if (!(Character.isJavaIdentifierPart(c) || c == ':' || c == '-')) {
                return false;
            }
        }
        return true;
    }

    private IRIResolver getIRIResolver() {
        return this.wswrlOntology.getIRIResolver();
    }

    @NonNull
    private OWLObjectProperty createOWLObjectProperty(String objectPropertyShortName)
            throws WSWRLParseException {
        if (isOWLObjectProperty(objectPropertyShortName)) {
            IRI propertyIRI = prefixedName2IRI(objectPropertyShortName);
            return this.getDataFactory().getOWLObjectProperty(propertyIRI);
        } else
            throw new WSWRLParseException(objectPropertyShortName + " is not an OWL object property");
    }

    @NonNull
    private OWLDataProperty createOWLDataProperty(String dataPropertyShortName)
            throws WSWRLParseException {
        if (isOWLDataProperty(dataPropertyShortName)) {
            IRI propertyIRI = prefixedName2IRI(dataPropertyShortName);
            return this.getDataFactory().getOWLDataProperty(propertyIRI);
        } else
            throw new WSWRLParseException(dataPropertyShortName + " is not an OWL data property");
    }

    @NonNull
    private IRI createSWRLBuiltInIRI(String builtInPrefixedName) throws WSWRLParseException {
        Optional<IRI> iri = this.getSWRLAPIOWLOntology().swrlBuiltInPrefixedName2IRI(builtInPrefixedName);

        if (iri.isPresent())
            return iri.get();
        else
            throw new WSWRLParseException(builtInPrefixedName + " is not a SWRL built-in");
    }
}
