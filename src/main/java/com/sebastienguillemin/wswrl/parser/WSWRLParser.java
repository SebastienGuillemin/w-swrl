package com.sebastienguillemin.wswrl.parser;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.parser.SWRLParser;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
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
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLIncompleteRuleException;
import com.sebastienguillemin.wswrl.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.rank.DefaultRank;


/**
 * A WSWRL parser that extends the SWRL API Parser.
 * 
 * @see org.swrlapi.parser.SWRLParser
 */
public class WSWRLParser extends SWRLParser {
    public final static char CONJUNCTION_CHAR = '^';
    public final static String IMP_COMBINATION = "->";
    public final static char RING_CHAR = '\u02da'; // .

    private static final String SAME_AS_PREDICATE = "sameAs";
    private static final String DIFFERENT_FROM_PREDICATE = "differentFrom";

    public static int findSplittingPoint(@NonNull String ruleText) {
        int i = ruleText.length() - 1;

        while (i >= 0 && !(WSWRLTokenizer.isOrdinaryChar(ruleText.charAt(i)) || ruleText.charAt(i) == '"'
                || ruleText.charAt(i) == ' '))
            i--;

        return i + 1;
    }

    @NonNull
    private final WSWRLParserSupport wswrlParserSupport;
    private final Hashtable<Integer, Rank> ranks;

    public WSWRLParser(@NonNull WSWRLOntology wswrlOntology) {
        super((SWRLAPIOWLOntology) wswrlOntology);
        this.wswrlParserSupport = new WSWRLParserSupport(wswrlOntology);
        this.ranks = new Hashtable<>();
    }

    /**
     * @param ruleName             The rule name
     * @param ruleText             The rule text
     * @param comment              A comment
     * @param interactiveParseOnly If True simply parse
     * @return The parsed rule
     * @throws WSWRLParseException If an error occurs during parsing
     */
    public Optional<com.sebastienguillemin.wswrl.core.rule.WSWRLRule> parseWSWRLRule(@NonNull String ruleName,
            @NonNull String ruleText, @NonNull String comment, boolean interactiveParseOnly)
            throws WSWRLParseException, MissingRankException {

        WSWRLTokenizer tokenizer = new WSWRLTokenizer(ruleText.trim(), interactiveParseOnly);
        Optional<Set<WSWRLAtom>> head = !tokenizer.isInteractiveParseOnly()
                ? Optional.of(this.wswrlParserSupport.createWSWRLHeadAtomList())
                : Optional.<Set<WSWRLAtom>>empty();
        Optional<Set<WSWRLAtom>> body = !tokenizer.isInteractiveParseOnly()
                ? Optional.of(this.wswrlParserSupport.createWSWRLBodyAtomList())
                : Optional.<Set<WSWRLAtom>>empty();
        boolean atLeastOneAtom = false, justProcessedAtom = false, isInHead = false;
        String message;
        int rankIndex = 0;

        if (!tokenizer.isInteractiveParseOnly() && !tokenizer.hasMoreTokens())
            throw new WSWRLParseException("Empty!");

        WSWRLToken lastToken = null, currentToken;
        do {
            if (justProcessedAtom)
                message = isInHead ? "Expecting " + CONJUNCTION_CHAR
                        : "Expecting " + IMP_COMBINATION + ", " + CONJUNCTION_CHAR + " or " + RING_CHAR;
            else
                message = isInHead ? "Expecting atom" : "Expecting atom," + IMP_COMBINATION + " or " + RING_CHAR;

            currentToken = tokenizer.getToken(message);

            if (currentToken.isImp()) { // An empty body is ok
                if (isInHead)
                    throw new WSWRLParseException("Second occurrence of ^");
                isInHead = true;
            } else if (currentToken.isConjunction()) {
                if (!justProcessedAtom)
                    throw new WSWRLParseException("^ may occur only after an atom");
            } else if (currentToken.isAnd()) {
                throw new WSWRLParseException("AND may be used only in a class or property expression");
            } else if (currentToken.isOr()) {
                throw new WSWRLParseException("OR may be used only in a class or property expression ");
            } else if (currentToken.isNot()) {
                throw new WSWRLParseException("NOT may be used only in a class or property expression");
            } else if (currentToken.isRing() && isInHead) {
                throw new WSWRLParseException(". may only occur in query body");
            } else if (currentToken.isStar()) {
                if (isInHead)
                    throw new WSWRLParseException("* may only occur in query body");
                else if (lastToken != null && lastToken.isInteger()) {
                    rankIndex = lastToken.getIntegerValue();
                }
            } else if (currentToken.isRank()) {
                rankIndex = Integer.parseInt(currentToken.getValue());
            } else if (currentToken.isLParen()) {
                // TODO Class or property expression
            } else if (currentToken.isShortName()) {
                String shortName = currentToken.getValue();
                WSWRLAtom atom = parseWSWRLAtom(ruleName, shortName, tokenizer, isInHead).get();
                Rank rank = this.getRank(rankIndex);
                atom.setRank(rank);

                atLeastOneAtom = true;
                if (!tokenizer.isInteractiveParseOnly())
                    if (isInHead)
                        head.get().add(atom);
                    else
                        body.get().add(atom);
                rankIndex = 0;
            } else if (currentToken.isIRI()) {
                String shortName = this.wswrlParserSupport.getShortNameFromIRI(currentToken.getValue(),
                        tokenizer.isInteractiveParseOnly());
                WSWRLAtom atom = parseWSWRLAtom(ruleName, shortName, tokenizer, isInHead).get();
                Rank rank = this.getRank(rankIndex);
                atom.setRank(rank);

                atLeastOneAtom = true;
                if (!tokenizer.isInteractiveParseOnly())
                    if (isInHead)
                        head.get().add(atom);
                    else
                        body.get().add(atom);
                rankIndex = 0;
            } else if (currentToken.isInteger()) {
                // DO Nothing.
            } else {
                throw new WSWRLParseException("Unexpected token '" + currentToken.getValue() + "'");
            }
            justProcessedAtom = true;
            lastToken = currentToken;
        } while (tokenizer.hasMoreTokens());

        if (!tokenizer.isInteractiveParseOnly()) {
            if (!atLeastOneAtom)
                throw new WSWRLParseException("Incomplete - no antecedent or consequent");
            return Optional
                    .of(this.wswrlParserSupport.createWSWRLRule(ruleName, head.get(), body.get(), true));
        } else
            return Optional.<com.sebastienguillemin.wswrl.core.rule.WSWRLRule>empty();
    }

    /**
     * If the rule is correct though possibly incomplete return <code>true</code>;
     * if the rule has errors return
     * <code>false</code>.
     *
     * @param ruleText The rule text
     * @return True if the rule is valid but possibly incomplete
     */
    public boolean isWSWRLRuleCorrectButPossiblyIncomplete(@NonNull String ruleText) {
        try {
            parseWSWRLRule("", ruleText, "", true);
            return true;
        } catch (WSWRLIncompleteRuleException e) {
            return true;
        } catch (WSWRLParseException | MissingRankException e) {
            return false;
        }
    }

    /**
     * If the rule is correct and complete return <code>true</code>; if the rule has
     * errors or is incomplete return
     * <code>false</code>.
     *
     * @param ruleText The rule text
     * @return True is the rule is correct and complete
     */
    public boolean isWSWRLRuleCorrectAndComplete(@NonNull String ruleText) {
        try {
            parseWSWRLRule("", ruleText, "", true);
            return true;
        } catch (WSWRLParseException | MissingRankException e) {
            return false;
        }
    }

    private Rank getRank(int rankIndex) {
        if (!this.ranks.containsKey(rankIndex)) {
            this.ranks.put(rankIndex, new DefaultRank(rankIndex));
        }

        return this.ranks.get(rankIndex);
    }

    private Optional<? extends WSWRLAtom> parseWSWRLAtom(String ruleName, @NonNull String shortName,
            @NonNull WSWRLTokenizer tokenizer, boolean isInHead) throws WSWRLParseException {

        if (shortName.equalsIgnoreCase(SAME_AS_PREDICATE)) {
            tokenizer.checkAndSkipLParen("Expecting parentheses-enclosed arguments for same individual atom");
            return parseWSWRLSameAsAtomArguments(tokenizer, isInHead);
        } else if (shortName.equalsIgnoreCase(DIFFERENT_FROM_PREDICATE)) {
            tokenizer.checkAndSkipLParen("Expecting parentheses-enclosed arguments for different individuals atom");
            return parseWSWRLDifferentFromAtomArguments(tokenizer, isInHead);
        } else if (this.wswrlParserSupport.isWSWRLBuiltIn(shortName)) {
            tokenizer.checkAndSkipLParen("Expecting parentheses-enclosed arguments for built-in atom");
            return parseWSWRLBuiltinAtomArguments(ruleName, shortName, tokenizer, isInHead);
        } else if (this.wswrlParserSupport.isOWLClass(shortName)) {
            tokenizer.checkAndSkipLParen("Expecting parentheses-enclosed arguments for class atom");
            return parseWSWRLClassAtomArguments(shortName, tokenizer, isInHead);
        } else if (this.wswrlParserSupport.isOWLObjectProperty(shortName)) {
            tokenizer.checkAndSkipLParen("Expecting parentheses-enclosed arguments for object property atom");
            return parseWSWRLObjectPropertyAtomArguments(shortName, tokenizer, isInHead);
        } else if (this.wswrlParserSupport.isOWLDataProperty(shortName)) {
            tokenizer.checkAndSkipLParen("Expecting parentheses-enclosed arguments for data property atom");
            return parseWSWRLDataPropertyAtomArguments(shortName, tokenizer, isInHead);
        } else if (this.wswrlParserSupport.isOWLDatatype(shortName)) {
            tokenizer.checkAndSkipLParen("Expecting parentheses-enclosed arguments for data range atom");
            return parseWSWRLDataRangeAtomArguments(shortName, tokenizer, isInHead);
        } else
            throw generateEndOfRuleException("Invalid SWRL atom predicate '" + shortName + "'", tokenizer);
    }

    private Optional<WSWRLClassAtom> parseWSWRLClassAtomArguments(@NonNull String classShortName,
            @NonNull WSWRLTokenizer tokenizer, boolean isInHead) throws WSWRLParseException {
        Optional<? extends @NonNull WSWRLIVariable> iVariable = parseWSWRLIVariable(tokenizer, isInHead);

        tokenizer.checkAndSkipRParen("Expecting closing parenthesis after argument for class atom " + classShortName);

        return !tokenizer.isInteractiveParseOnly()
                ? Optional.of(this.wswrlParserSupport.createWSWRLClassAtom(classShortName, iVariable.get()))
                : Optional.<WSWRLClassAtom>empty();
    }

    private Optional<WSWRLObjectPropertyAtom> parseWSWRLObjectPropertyAtomArguments(
            @NonNull String propertyShortName, @NonNull WSWRLTokenizer tokenizer, boolean isInHead)
            throws WSWRLParseException {
        Optional<? extends @NonNull WSWRLIVariable> iVariable1 = parseWSWRLIVariable(tokenizer, isInHead);
        tokenizer
                .checkAndSkipComma(
                        "Expecting comma-separated second argument for object property atom " + propertyShortName);
        Optional<? extends @NonNull WSWRLIVariable> iVariable2 = parseWSWRLIVariable(tokenizer, isInHead);
        tokenizer.checkAndSkipRParen(
                "Expecting closing parenthesis after second argument of object property atom " + propertyShortName);

        return !tokenizer.isInteractiveParseOnly() ? Optional.of(
                this.wswrlParserSupport.createWSWRLObjectPropertyAtom(propertyShortName, iVariable1.get(),
                        iVariable2.get()))
                : Optional.<WSWRLObjectPropertyAtom>empty();
    }

    private Optional<WSWRLDataPropertyAtom> parseWSWRLDataPropertyAtomArguments(
            @NonNull String propertyShortName,
            @NonNull WSWRLTokenizer tokenizer, boolean isInHead) throws WSWRLParseException {
        Optional<? extends @NonNull WSWRLIVariable> iVariable = parseWSWRLIVariable(tokenizer, isInHead);
        tokenizer
                .checkAndSkipComma(
                        "Expecting comma-separated second parameter for data property atom " + propertyShortName);
        Optional<? extends @NonNull WSWRLDVariable> dVariables = parseWSWRLDVariable(tokenizer, isInHead, false);
        tokenizer.checkAndSkipRParen(
                "Expecting closing parenthesis after second argument of data property atom " + propertyShortName);

        return !tokenizer.isInteractiveParseOnly() ? Optional
                .of(this.wswrlParserSupport.createWSWRLDataPropertyAtom(propertyShortName, iVariable.get(),
                        dVariables.get()))
                : Optional.<WSWRLDataPropertyAtom>empty();
    }

    private Optional<WSWRLBuiltInAtom> parseWSWRLBuiltinAtomArguments(String ruleName, @NonNull String builtInPrefixedName,
            @NonNull WSWRLTokenizer tokenizer, boolean isInHead) throws WSWRLParseException {
        Optional<@NonNull List<@NonNull WSWRLDVariable>> dVariablesList = parseWSWRLDVariableList(tokenizer,
                isInHead); // Swallows ')'

        return !tokenizer.isInteractiveParseOnly()
                ? Optional.of(this.wswrlParserSupport.createWSWRLBuiltInAtom(ruleName, builtInPrefixedName, dVariablesList.get()))
                : Optional.<WSWRLBuiltInAtom>empty();
    }

    private Optional<WSWRLDataRangeAtom> parseWSWRLDataRangeAtomArguments(@NonNull String datatypePrefixedName,
            @NonNull WSWRLTokenizer tokenizer, boolean isInHead) throws WSWRLParseException {
        Optional<? extends @NonNull WSWRLDVariable> dVariables = parseWSWRLDVariable(tokenizer, isInHead, false);

        tokenizer
                .checkAndSkipRParen(
                        "Expecting closing parenthesis after argument for data range atom " + datatypePrefixedName);

        return !tokenizer.isInteractiveParseOnly()
                ? Optional.of(this.wswrlParserSupport.createWSWRLDataRangeAtom(datatypePrefixedName, dVariables.get()))
                : Optional.empty();
    }

    private Optional<WSWRLSameIndividualsAtom> parseWSWRLSameAsAtomArguments(@NonNull WSWRLTokenizer tokenizer,
            boolean isInHead) throws WSWRLParseException {
        Optional<? extends @NonNull WSWRLIVariable> iVariable1 = parseWSWRLIVariable(tokenizer, isInHead);
        tokenizer.checkAndSkipComma("Expecting comma-separated second argument for same individual atom");
        Optional<? extends @NonNull WSWRLIVariable> iVariable2 = parseWSWRLIVariable(tokenizer, isInHead);
        tokenizer.checkAndSkipRParen("Expecting closing parenthesis after second argument to same individual atom");

        return tokenizer.isInteractiveParseOnly() ? Optional.<WSWRLSameIndividualsAtom>empty()
                : Optional
                        .of(this.wswrlParserSupport.createWSWRLSameIndividualAtom(iVariable1.get(), iVariable2.get()));
    }

    private Optional<WSWRLDifferentIndividualsAtom> parseWSWRLDifferentFromAtomArguments(
            @NonNull WSWRLTokenizer tokenizer, boolean isInHead) throws WSWRLParseException {
        Optional<? extends @NonNull WSWRLIVariable> iVariable1 = parseWSWRLIVariable(tokenizer, isInHead);
        tokenizer.checkAndSkipComma("Expecting comma-separated second argument for different individuals atom");
        Optional<? extends @NonNull WSWRLIVariable> iVariable2 = parseWSWRLIVariable(tokenizer, isInHead);

        tokenizer.checkAndSkipRParen(
                "Expecting closing parenthesis after second argument to different individuals atom");

        return tokenizer.isInteractiveParseOnly() ? Optional.<WSWRLDifferentIndividualsAtom>empty()
                : Optional.of(
                        this.wswrlParserSupport.createWSWRLDifferentIndividualsAtom(iVariable1.get(),
                                iVariable2.get()));
    }

    private Optional<WSWRLVariable> parseWSWRLVariable(@NonNull WSWRLTokenizer tokenizer, boolean isInHead,
            WSWRLVariableDomain domain)
            throws WSWRLParseException {
        WSWRLToken token = tokenizer.getToken(WSWRLToken.WSWRLTokenType.SHORTNAME, "Expecting variable name after ?");
        String variableName = token.getValue();

        this.wswrlParserSupport.checkThatWSWRLVariableNameIsValid(variableName);

        if (tokenizer.hasMoreTokens()) {
            if (!isInHead)
                tokenizer.addVariable(variableName);
            else if (!tokenizer.hasVariable(variableName))
                throw new WSWRLParseException(
                        "Variable ?" + variableName + " used in consequent is not present in antecedent");
        }

        if (tokenizer.isInteractiveParseOnly() || domain == WSWRLVariableDomain.UNKNOWN)
            return Optional.<WSWRLVariable>empty();

        else if (domain == WSWRLVariableDomain.INDIVIDUALS)
            return Optional.of(this.wswrlParserSupport.createWSWRLIVariable(variableName));
        else
            return Optional.of(this.wswrlParserSupport.createWSWRLDVariable(variableName));
    }

    private Optional<WSWRLIVariable> parseWSWRLIVariable(@NonNull WSWRLTokenizer tokenizer,
            boolean isInHead) throws WSWRLParseException { // Parse a SWRL variable or an OWL named individual
        WSWRLToken token = tokenizer.getToken("Expecting variable or OWL individual name");

        if (token.isQuestion())
            return Optional.of(
                    (WSWRLIVariable) parseWSWRLVariable(tokenizer, isInHead, WSWRLVariableDomain.INDIVIDUALS).get());
        else if (token.isShortName()) {
            String identifier = token.getValue();
            if (this.wswrlParserSupport.isOWLNamedIndividual(identifier)) {
                return !tokenizer.isInteractiveParseOnly()
                        ? Optional.of(this.wswrlParserSupport.createWSWRLIndividualArgument(identifier))
                        : Optional.<@NonNull WSWRLIVariable>empty();
            } else
                throw generateEndOfRuleException("Invalid OWL individual name '" + token.getValue() + "'", tokenizer);
        } else
            throw new WSWRLParseException("Expecting variable or OWL individual name, got '" + token.getValue() + "'");
    }

    private Optional<WSWRLDVariable> parseWSWRLDVariable(@NonNull WSWRLTokenizer tokenizer,
            boolean isInHead, boolean isInBuiltIn) throws WSWRLParseException { // Parse a SWRL variable or an OWL
                                                                                // literal; if we are processing
                                                                                // built-in
                                                                                // arguments we also allow OWL entity
                                                                                // names
        String message = isInBuiltIn ? "Expecting variable, literal or OWL entity name for built-in atom argument"
                : "Expecting variable or literal for datatype atom argument";
        WSWRLToken token = tokenizer.getToken(message);

        if (token.isQuestion())
            return Optional
                    .of((WSWRLDVariable) parseWSWRLVariable(tokenizer, isInHead, WSWRLVariableDomain.DATA).get());
        else if (token.isShortName()) {
            String shortName = token.getValue();
            return parseShortNameWSWRLDVariable(tokenizer, isInBuiltIn, shortName);
        } else if (token.isString()) {
            String literalValue = token.getValue();
            return parseLiteralWSWRLDVariable(tokenizer, literalValue);
        } else if (token.isInteger()) {
            return !tokenizer.isInteractiveParseOnly()
                    ? Optional.of(this.wswrlParserSupport.createXSDIntegerSWRLLiteralArgument(token.getValue()))
                    : Optional.<@NonNull WSWRLDVariable>empty();
        } else if (token.isDecimal()) {
            return !tokenizer.isInteractiveParseOnly()
                    ? Optional.of(this.wswrlParserSupport.createXSDDecimalSWRLLiteralArgument(token.getValue()))
                    : Optional.<@NonNull WSWRLDVariable>empty();
        } else
            throw new WSWRLParseException(
                    "Expecting variable, literal or OWL entity name, got '" + token.getValue() + "'");
    }

    private Optional<@NonNull WSWRLDVariable> parseLiteralWSWRLDVariable(@NonNull WSWRLTokenizer tokenizer,
            @NonNull String literalValue) throws WSWRLParseException {
        if (tokenizer.peekToken("String may be qualified with datatype").isConjunction()) {
            tokenizer.skipToken(); // Skip the peeked token
            throw generateEndOfRuleException("Partial datatype qualifier - add '^' to complete", tokenizer);
        } else if (tokenizer.peekToken("String may be qualified with datatype").isTypeQualifier()) {
            tokenizer.skipToken(); // Skip the peeked token
            WSWRLToken datatypeToken = tokenizer.getToken(WSWRLToken.WSWRLTokenType.SHORTNAME,
                    "Expecting datatype after ^^");
            String datatype = datatypeToken.getValue();
            if (datatype.length() == 0)
                throw generateEndOfRuleException("Empty datatype qualifier - must supply a datatype after ^^",
                        tokenizer);
            else if (!this.wswrlParserSupport.isOWLDatatype(datatype))
                throw generateEndOfRuleException("invalid datatype name '" + datatype + "'", tokenizer);
            return !tokenizer.isInteractiveParseOnly()
                    ? Optional.of(this.wswrlParserSupport.createSWRLLiteralArgument(literalValue, datatype))
                    : Optional.<@NonNull WSWRLDVariable>empty();
        } else
            return !tokenizer.isInteractiveParseOnly()
                    ? Optional.of(this.wswrlParserSupport.createXSDStringSWRLLiteralArgument(literalValue))
                    : Optional.<@NonNull WSWRLDVariable>empty();
    }

    /**
     * The OWLAPI follows the OWL Specification and does not explicitly allow named
     * OWL entities as arguments to
     * built-ins. However, if OWLAPI parsers encounter OWL entities as parameters
     * they appear to represent them as SWRL
     * variables - with the variable IRI set to the IRI of the entity
     * ({@link org.semanticweb.owlapi.model.OWLEntity}
     * classes represent named OWL concepts so have an IRI). So if we are processing
     * built-in parameters and encounter
     * variables with an IRI referring to OWL entities in the active ontology we can
     * transform them to the
     * appropriate SWRLAPI built-in argument for the named entity.
     *
     * @see org.swrlapi.parser.SWRLParser#parseShortNameSWRLdVariables(WSWRLTokenizer,
     *      boolean, String)
     */
    private Optional<@NonNull WSWRLDVariable> parseShortNameWSWRLDVariable(@NonNull WSWRLTokenizer tokenizer,
            boolean isInBuiltIn, @NonNull String shortName) throws WSWRLParseException {
        // We allow the values "true" and "false" and interpret them as OWL literals of
        // type xsd:boolean.
        if (shortName.equalsIgnoreCase("true") || shortName.equalsIgnoreCase("false")) {
            return !tokenizer.isInteractiveParseOnly()
                    ? Optional.of(this.wswrlParserSupport.createXSDBooleanSWRLLiteralArgument(shortName))
                    : Optional.<@NonNull WSWRLDVariable>empty();
        } else { // Not "true" or "false"
            if (isInBuiltIn) { // SWRL built-ins in the SWRLAPI allow OWL entity names as arguments
                if (this.wswrlParserSupport.isOWLNamedIndividual(shortName)) {
                    return tokenizer.isInteractiveParseOnly() ? Optional.<@NonNull WSWRLDVariable>empty()
                            : Optional.of(this.wswrlParserSupport.createSWRLNamedIndividualBuiltInArgument(shortName));
                } else if (this.wswrlParserSupport.isOWLClass(shortName)) {
                    return tokenizer.isInteractiveParseOnly() ? Optional.<@NonNull WSWRLDVariable>empty()
                            : Optional.of(this.wswrlParserSupport.createSWRLClassBuiltInArgument(shortName));
                } else if (this.wswrlParserSupport.isOWLObjectProperty(shortName)) {
                    return tokenizer.isInteractiveParseOnly() ? Optional.<@NonNull WSWRLDVariable>empty()
                            : Optional.of(this.wswrlParserSupport.createSWRLObjectPropertyBuiltInArgument(shortName));
                } else if (this.wswrlParserSupport.isOWLDataProperty(shortName)) {
                    return tokenizer.isInteractiveParseOnly() ? Optional.<@NonNull WSWRLDVariable>empty()
                            : Optional.of(this.wswrlParserSupport.createSWRLDataPropertyBuiltInArgument(shortName));
                } else if (this.wswrlParserSupport.isOWLAnnotationProperty(shortName)) {
                    return tokenizer.isInteractiveParseOnly() ? Optional.<@NonNull WSWRLDVariable>empty()
                            : Optional
                                    .of(this.wswrlParserSupport.createSWRLAnnotationPropertyBuiltInArgument(shortName));
                } else if (this.wswrlParserSupport.isOWLDatatype(shortName)) {
                    return tokenizer.isInteractiveParseOnly() ? Optional.<@NonNull WSWRLDVariable>empty()
                            : Optional.of(this.wswrlParserSupport.createSWRLDatatypeBuiltInArgument(shortName));
                } else
                    throw generateEndOfRuleException("Expecting boolean or OWL entity name, got '" + shortName + "'",
                            tokenizer);
            } else
                // Not "true" or "false" and not a built-in argument
                throw generateEndOfRuleException("Expecting boolean, got '" + shortName + "'", tokenizer);
        }
    }

    private Optional<@NonNull List<@NonNull WSWRLDVariable>> parseWSWRLDVariableList(@NonNull WSWRLTokenizer tokenizer,
            boolean isInHead) throws WSWRLParseException { // Parse an argument list that can contain variables, OWL
                                                           // named entities, and literals
        Optional<@NonNull List<@NonNull WSWRLDVariable>> dVariabless = !tokenizer.isInteractiveParseOnly()
                ? Optional.of(new ArrayList<>())
                : Optional.<@NonNull List<@NonNull WSWRLDVariable>>empty();

        Optional<? extends @NonNull WSWRLDVariable> dVariables = parseWSWRLDVariable(tokenizer, isInHead, true);

        if (!tokenizer.isInteractiveParseOnly())
            dVariabless.get().add(dVariables.get());

        WSWRLToken token = tokenizer
                .getToken("Expecting additional comma-separated built-in arguments or closing parenthesis");
        while (token.isComma()) {
            dVariables = parseWSWRLDVariable(tokenizer, isInHead, true);
            if (!tokenizer.isInteractiveParseOnly())
                dVariabless.get().add(dVariables.get());
            token = tokenizer.getToken("Expecting ',' or ')'");
            if (!(token.isComma() || token.isRParen()))
                throw new WSWRLParseException("Expecting ',' or ')', got '" + token.getValue() + "'");
        }
        return dVariabless;
    }

    @NonNull
    private WSWRLParseException generateEndOfRuleException(@NonNull String message,
            @NonNull WSWRLTokenizer tokenizer) {
        if (tokenizer.hasMoreTokens() || !tokenizer.isInteractiveParseOnly())
            return new WSWRLParseException(message);
        else
            return new WSWRLIncompleteRuleException(message);
    }
}
