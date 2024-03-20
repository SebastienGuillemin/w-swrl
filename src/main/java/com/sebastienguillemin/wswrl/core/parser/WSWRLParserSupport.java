package com.sebastienguillemin.wswrl.core.parser;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.XSDVocabulary;
import org.swrlapi.core.IRIResolver;

import com.sebastienguillemin.wswrl.core.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.WSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.core.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.WSWRLDataRangeAtom;
import com.sebastienguillemin.wswrl.core.WSWRLDifferentIndividualsAtom;
import com.sebastienguillemin.wswrl.core.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.WSWRLSameIndividualAtom;
import com.sebastienguillemin.wswrl.core.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.core.exception.MissingRankException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;

public class WSWRLParserSupport {
    @NonNull
    private final WSWRLOntology wswrlOntology;

    public WSWRLParserSupport(WSWRLOntology wswrlOntology) {
        this.wswrlOntology = wswrlOntology;
    }

    public Set<WSWRLAtom> createWSWRLHeadAtomList() {
        return new LinkedHashSet<>();
    }

    public Set<WSWRLAtom> createWSWRLBodyAtomList() {
        return new LinkedHashSet<>();
    }

    public String getShortNameFromIRI(@NonNull String value, boolean interactiveParseOnly) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getShortNameFromIRI'");
    }

    public WSWRLRule createWSWRLRule(String ruleName, Set<WSWRLAtom> head, Set<WSWRLAtom> body, boolean isEnabled) throws MissingRankException {
        // TODO : gérer le cas d'ajout d'une annotation.
        return getWSWRLDataFactory().getWSWRLRule(ruleName, head, body, isEnabled);
    }

    public boolean isOWLEntity(@NonNull String shortName) {
        return isOWLClass(shortName) || isOWLNamedIndividual(shortName) || isOWLObjectProperty(shortName)
                || isOWLDataProperty(shortName) || isOWLAnnotationProperty(shortName) || isOWLDatatype(shortName);
    }

    public boolean isOWLClass(@NonNull String shortName) {
        IRI classIRI = prefixedName2IRI(shortName);

        return getOWLOntology().containsClassInSignature(classIRI, Imports.INCLUDED) || classIRI
                .equals(OWLRDFVocabulary.OWL_THING.getIRI()) || classIRI.equals(OWLRDFVocabulary.OWL_NOTHING.getIRI());
    }

    public boolean isWSWRLBuiltIn(@NonNull String shortName) {
        return this.wswrlOntology.isSWRLBuiltIn(shortName);
    }

    public boolean isOWLObjectProperty(@NonNull String shortName) {
        IRI propertyIRI = prefixedName2IRI(shortName);
        return this.getOWLOntology().containsObjectPropertyInSignature(propertyIRI, Imports.INCLUDED);
    }

    public boolean isOWLDataProperty(@NonNull String shortName) {
        IRI propertyIRI = prefixedName2IRI(shortName);
        return this.getOWLOntology().containsDataPropertyInSignature(propertyIRI, Imports.INCLUDED);
    }

    public boolean isOWLDatatype(@NonNull String shortName) {
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

    public WSWRLClassAtom createWSWRLClassAtom(@NonNull String classShortName, @NonNull SWRLIArgument iArgument)
            throws WSWRLParseException {
        OWLClass cls = createOWLClass(classShortName);

        return getWSWRLDataFactory().getWSWRLClassAtom(cls, iArgument);
    }

    public WSWRLObjectPropertyAtom createWSWRLObjectPropertyAtom(@NonNull String propertyShortName,
            @NonNull SWRLIArgument swrliArgument,
            @NonNull SWRLIArgument swrliArgument2) throws WSWRLParseException {
        OWLObjectProperty objectProperty = createOWLObjectProperty(propertyShortName);

        return getWSWRLDataFactory().getWSWRLObjectPropertyAtom(objectProperty, swrliArgument, swrliArgument2);
    }

    public WSWRLDataPropertyAtom createWSWRLDataPropertyAtom(@NonNull String propertyShortName,
            @NonNull SWRLIArgument swrliArgument,
            @NonNull SWRLDArgument swrldArgument) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLDataPropertyAtom'");
    }

    public WSWRLBuiltInAtom createWSWRLBuiltInAtom(@NonNull String builtInPrefixedName,
            @NonNull List<@NonNull SWRLDArgument> list) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLBuiltInAtom'");
    }

    public WSWRLDataRangeAtom createWSWRLDataRangeAtom(@NonNull String datatypePrefixedName,
            @NonNull SWRLDArgument swrldArgument) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLDataRangeAtom'");
    }

    public WSWRLSameIndividualAtom createWSWRLSameIndividualAtom(@NonNull SWRLIArgument swrliArgument,
            @NonNull SWRLIArgument swrliArgument2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLSameIndividualAtom'");
    }

    public WSWRLDifferentIndividualsAtom createWSWRLDifferentIndividualsAtom(@NonNull SWRLIArgument swrliArgument,
            @NonNull SWRLIArgument swrliArgument2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLDifferentIndividualsAtom'");
    }

    public void checkThatWSWRLVariableNameIsValid(@NonNull String variableName) throws WSWRLParseException {
        if (!isValidSWRLVariableName(variableName))
            throw new WSWRLParseException("Invalid WSWRL variable name " + variableName);

        if (isOWLEntity(variableName))
            throw new WSWRLParseException("Invalid WSWRL variable name " + variableName
                    + " - cannot use name of existing OWL class, individual, property, or datatype");
    }

    public @NonNull WSWRLVariable createWSWRLVariable(@NonNull String variableName, WSWRLVariableDomain domain) throws WSWRLParseException {
        if (isOWLEntity(variableName))
            throw new WSWRLParseException(variableName
                    + " cannot be used as a WSWRL variable name because it refers to an existing OWL entity");

        Optional<IRI> iri = getIRIResolver().variableName2IRI(variableName);

        if (iri.isPresent())
            return getWSWRLDataFactory().getWSWRLVariable(iri.get(), domain);
        else
            throw new WSWRLParseException("error creating WSWRL variable " + variableName);
    }
    
    public boolean isOWLNamedIndividual(@NonNull String shortName) {
        IRI individualIRI = prefixedName2IRI(shortName);
        return getOWLOntology().containsIndividualInSignature(individualIRI, Imports.INCLUDED);
    }

    public SWRLIArgument createSWRLIndividualArgument(String identifier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLIndividualArgument'");
    }

    public SWRLDArgument createXSDIntegerSWRLLiteralArgument(@NonNull String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createXSDIntegerSWRLLiteralArgument'");
    }

    public SWRLDArgument createXSDDecimalSWRLLiteralArgument(@NonNull String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createXSDDecimalSWRLLiteralArgument'");
    }

    public SWRLDArgument createSWRLLiteralArgument(@NonNull String literalValue, String datatype) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLLiteralArgument'");
    }

    public SWRLDArgument createXSDStringSWRLLiteralArgument(@NonNull String literalValue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createXSDStringSWRLLiteralArgument'");
    }

    public SWRLDArgument createXSDBooleanSWRLLiteralArgument(@NonNull String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createXSDBooleanSWRLLiteralArgument'");
    }

    public SWRLDArgument createSWRLNamedIndividualBuiltInArgument(@NonNull String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLNamedIndividualBuiltInArgument'");
    }

    public SWRLDArgument createSWRLClassBuiltInArgument(@NonNull String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLClassBuiltInArgument'");
    }

    public SWRLDArgument createSWRLObjectPropertyBuiltInArgument(@NonNull String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLObjectPropertyBuiltInArgument'");
    }

    public SWRLDArgument createSWRLDataPropertyBuiltInArgument(@NonNull String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLDataPropertyBuiltInArgument'");
    }

    public boolean isOWLAnnotationProperty(@NonNull String shortName) {
        IRI propertyIRI = prefixedName2IRI(shortName);
        return getOWLOntology().containsAnnotationPropertyInSignature(propertyIRI, Imports.INCLUDED);
    }

    public SWRLDArgument createSWRLAnnotationPropertyBuiltInArgument(@NonNull String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLAnnotationPropertyBuiltInArgument'");
    }

    public SWRLDArgument createSWRLDatatypeBuiltInArgument(@NonNull String shortName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSWRLDatatypeBuiltInArgument'");
    }

    @NonNull
    private OWLClass createOWLClass(@NonNull String classShortName) throws WSWRLParseException {
        if (isOWLClass(classShortName)) {
            IRI classIRI = prefixedName2IRI(classShortName);
            return getOWLDataFactory().getOWLClass(classIRI);
        } else
            throw new WSWRLParseException(classShortName + " is not an OWL class");
    }

    @NonNull
    private OWLOntology getOWLOntology() {
        return this.wswrlOntology.getOWLOntology();
    }

    @NonNull
    private OWLDataFactory getOWLDataFactory() {
        return this.wswrlOntology.getSWRLAPIOWLDataFactory();
    }

    @NonNull
    private WSWRLDataFactory getWSWRLDataFactory() {
        return this.wswrlOntology.getWSWRLDataFactory();
    }

    private IRI prefixedName2IRI(String prefixedName) {
        Optional<@NonNull IRI> iri = getIRIResolver().prefixedName2IRI(prefixedName);

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
    private boolean isValidSWRLVariableName(@NonNull String candidateSWRLVariableName) {
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
    private OWLObjectProperty createOWLObjectProperty(@NonNull String objectPropertyShortName)
            throws WSWRLParseException {
        if (isOWLObjectProperty(objectPropertyShortName)) {
            IRI propertyIRI = prefixedName2IRI(objectPropertyShortName);
            return getOWLDataFactory().getOWLObjectProperty(propertyIRI);
        } else
            throw new WSWRLParseException(objectPropertyShortName + " is not an OWL object property");
    }
}
