package com.sebastienguillemin.wswrl.core.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.initialization.qual.UnderInitialization;
import org.checkerframework.checker.nullness.qual.NonNull;

import com.sebastienguillemin.wswrl.core.exception.WSWRLIncompleteRuleException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;

/**
 * Tokenizer generates a
 * {@link com.sebastienguillemin.wswrl.core.exception.WSWRLParseException}
 * for
 * invalid input and a
 * {@link org.swrlapi.parser.SWRLIncompleteRuleException} (which is a subclass
 * of
 * {@link com.sebastienguillemin.wswrl.core.exception.WSWRLParseException})
 * for valid but incomplete
 * input.
 *
 * ***Note :* a part of the code of this class is directly taken from the
 * open-source class: {@link org.swrlapi.parser.SWRLTokenizer}.**
 * 
 * @see org.swrlapi.parser.WSWRLToken
 * @see org.swrlapi.parser.SWRLParser
 * @see com.sebastienguillemin.wswrl.core.exception.WSWRLParseException
 * @see org.swrlapi.parser.SWRLIncompleteRuleException
 */
class WSWRLTokenizer {
    @NonNull
    private static final char[] wordChars = { ':', '_', '-', '/', '#' };
    @NonNull
    private static final char[] ordinaryChars = { '.', '^', '<', '>', '(', ')', '?' };

    @NonNull
    private final MyStreamTokenizer tokenizer;

    @NonNull
    private final Set<@NonNull String> swrlVariables;
    @NonNull
    private final List<@NonNull WSWRLToken> tokens;
    private final boolean interactiveParseOnly;
    private int tokenPosition;

    public WSWRLTokenizer(@NonNull String input, boolean interactiveParseOnly) throws WSWRLParseException {
        this.tokenizer = new MyStreamTokenizer(new StringReader(input));

        this.swrlVariables = new HashSet<>();
        this.interactiveParseOnly = interactiveParseOnly;

        for (char wordChar : wordChars)
            this.tokenizer.wordChars(wordChar, wordChar);

        this.tokenizer.wordChars('0', '9');

        for (char ordinaryChar : ordinaryChars)
            this.tokenizer.ordinaryChar(ordinaryChar);

        this.tokens = generateTokens();
        this.tokenPosition = 0;
    }

    public void reset() {
        this.tokenPosition = 0;
    }

    @NonNull
    public WSWRLToken getToken(WSWRLToken.WSWRLTokenType expectedTokenType, @NonNull String unexpectedTokenMessage)
            throws WSWRLParseException {
        if (hasMoreTokens()) {
            WSWRLToken token = getToken();

            if (token.getTokenType() == expectedTokenType)
                return token;
            else
                throw new WSWRLParseException(unexpectedTokenMessage);
        } else
            throw generateEndOfRuleException(unexpectedTokenMessage);
    }

    @NonNull
    public WSWRLToken getToken(@NonNull String noTokenMessage) throws WSWRLParseException {
        if (hasMoreTokens())
            return getToken();
        else
            throw generateEndOfRuleException(noTokenMessage);
    }

    public boolean hasMoreTokens() {
        return this.tokenPosition < this.tokens.size();
    }

    @NonNull
    public WSWRLToken peekToken(@NonNull String message) throws WSWRLParseException {
        if (this.tokenPosition < this.tokens.size())
            return this.tokens.get(this.tokenPosition);
        else
            throw generateEndOfRuleException(message);
    }

    public void skipToken() throws WSWRLParseException {
        if (this.tokenPosition < this.tokens.size())
            this.tokenPosition++;
        else
            throw generateEndOfRuleException("End of rule reached unexpectedly!");
    }

    public boolean isInteractiveParseOnly() {
        return this.interactiveParseOnly;
    }

    public boolean hasVariable(@NonNull String variableName) {
        return this.swrlVariables.contains(variableName);
    }

    public void addVariable(@NonNull String variableName) {
        this.swrlVariables.add(variableName);
    }

    public void checkAndSkipLParen(@NonNull String unexpectedTokenMessage) throws WSWRLParseException {
        checkAndSkipToken(WSWRLToken.WSWRLTokenType.LPAREN, unexpectedTokenMessage);
    }

    public void checkAndSkipRParen(@NonNull String unexpectedTokenMessage) throws WSWRLParseException {
        checkAndSkipToken(WSWRLToken.WSWRLTokenType.RPAREN, unexpectedTokenMessage);
    }

    public void checkAndSkipComma(@NonNull String unexpectedTokenMessage) throws WSWRLParseException {
        checkAndSkipToken(WSWRLToken.WSWRLTokenType.COMMA, unexpectedTokenMessage);
    }

    public static boolean isOrdinaryChar(char c) {
        for (char ordinaryChar : ordinaryChars) {
            if (ordinaryChar == c)
                return true;
        }
        return false;
    }

    @NonNull
    private WSWRLToken getToken() throws WSWRLParseException {
        if (this.tokenPosition < this.tokens.size())
            return this.tokens.get(this.tokenPosition++);
        else
            throw generateEndOfRuleException("Incomplete rule!");
    }

    private void checkAndSkipToken(WSWRLToken.WSWRLTokenType tokenType, @NonNull String unexpectedTokenMessage)
            throws WSWRLParseException {
        if (hasMoreTokens()) {
            WSWRLToken token = getToken();

            if (token.getTokenType() != tokenType)
                throw new WSWRLParseException(unexpectedTokenMessage + ", got '" + token.getValue() + "'");
        } else
            throw generateEndOfRuleException(unexpectedTokenMessage);
    }

    @UnderInitialization
    @NonNull
    private List<@NonNull WSWRLToken> generateTokens() throws WSWRLParseException {
        List<@NonNull WSWRLToken> tokens = new ArrayList<>();
        WSWRLToken token = generateToken();
        while (token.getTokenType() != WSWRLToken.WSWRLTokenType.END_OF_INPUT) {
            tokens.add(token);
            token = generateToken();
        }
        return tokens;
    }

    @NonNull
    private WSWRLToken generateToken() throws WSWRLParseException {
        try {
            return convertToken2WSWRLToken(this.tokenizer.nextToken());
        } catch (IOException e) {
            throw new WSWRLParseException("Error tokenizing " + (e.getMessage() != null ? e.getMessage() : ""));
        }
    }

    @NonNull
    private WSWRLToken convertToken2WSWRLToken(int tokenType) throws WSWRLParseException, IOException {

        switch (tokenType) {
            case StreamTokenizer.TT_EOF:
                return new WSWRLToken(WSWRLToken.WSWRLTokenType.END_OF_INPUT, "");
            case StreamTokenizer.TT_EOL:
                return new WSWRLToken(WSWRLToken.WSWRLTokenType.END_OF_INPUT, "");
            case StreamTokenizer.TT_NUMBER:
                    throw new WSWRLParseException("internal error - not expecting a number");
            case StreamTokenizer.TT_WORD: {
                String value = this.tokenizer.getValue();
                if (value.equalsIgnoreCase("not")) {
                    return new WSWRLToken(WSWRLToken.WSWRLTokenType.NOT, "not");
                } else if (value.equalsIgnoreCase("and")) {
                    return new WSWRLToken(WSWRLToken.WSWRLTokenType.AND, "and");
                } else if (value.equalsIgnoreCase("or")) {
                    return new WSWRLToken(WSWRLToken.WSWRLTokenType.OR, "or");
                } else {
                    boolean negativeNumeric = false;
                    if (value.equals("-")) {
                        int nextTokenType = this.tokenizer.nextToken();
                        if (nextTokenType == '>')
                            return new WSWRLToken(WSWRLToken.WSWRLTokenType.IMP, "->");
                        else if (nextTokenType == StreamTokenizer.TT_EOF)
                            throw generateEndOfRuleException("Expecting '>' or integer or decimal after '-'");
                        else if (nextTokenType != StreamTokenizer.TT_WORD)
                            throw new WSWRLParseException("Expecting '>' or integer or decimal after '-'");
                        else
                            negativeNumeric = true;
                    }
                    if (isInteger(value)) {
                        // See if it is followed by a '.', in which case it should be a decimal
                        if (this.tokenizer.nextToken() == '.') { // Found a . so expecting rest of decimal
                            int trailingTokenType = this.tokenizer.nextToken();
                            String trailingValue = this.tokenizer.getValue();
                            if (trailingTokenType == StreamTokenizer.TT_WORD && isInteger(trailingValue)) {
                                String decimalValue = value + "." + trailingValue;
                                decimalValue = negativeNumeric ? "-" + decimalValue : decimalValue;
                                return new WSWRLToken(WSWRLToken.WSWRLTokenType.DECIMAL, decimalValue);
                            } else if (trailingTokenType == StreamTokenizer.TT_EOF)
                                throw generateEndOfRuleException("Expecting decimal fraction part after '.'");
                            else
                                throw new WSWRLParseException("Expecting decimal fraction part after '.'");
                        } else { // No following '.' so it is an integer
                            this.tokenizer.pushBack();
                            String integerValue = negativeNumeric ? "-" + value : value;
                            return new WSWRLToken(WSWRLToken.WSWRLTokenType.INTEGER, integerValue);
                        }
                    } else { // Value is not an integer
                        if (negativeNumeric) // If negative, value should be an integer or decimal
                            throw new WSWRLParseException("Expecting integer or decimal");
                        else // Must be an identifier
                            return new WSWRLToken(WSWRLToken.WSWRLTokenType.SHORTNAME, value);
                    }
                }
            }
            case '*':
                return new WSWRLToken(WSWRLToken.WSWRLTokenType.STAR, "*");
            case '"':
                return new WSWRLToken(WSWRLToken.WSWRLTokenType.STRING, this.tokenizer.getValue());
            case ',':
                return new WSWRLToken(WSWRLToken.WSWRLTokenType.COMMA, ",");
            case '?':
                return new WSWRLToken(WSWRLToken.WSWRLTokenType.QUESTION, "?");
            case '(':
                return new WSWRLToken(WSWRLToken.WSWRLTokenType.LPAREN, "(");
            case ')':
                return new WSWRLToken(WSWRLToken.WSWRLTokenType.RPAREN, ")");
            case '.':
                return new WSWRLToken(WSWRLToken.WSWRLTokenType.RING, ".");
            case '^': {
                int nextTokenType = this.tokenizer.nextToken();
                if (nextTokenType == '^') {
                    return new WSWRLToken(WSWRLToken.WSWRLTokenType.TYPE_QUAL, "^^");
                } else { // Not ^^
                    this.tokenizer.pushBack();
                    return new WSWRLToken(WSWRLToken.WSWRLTokenType.CONJUNCTION, "^");
                }
            }
            case '<': {
                int nextTokenType = this.tokenizer.nextToken();
                if (nextTokenType == StreamTokenizer.TT_WORD) {
                    String iri = this.tokenizer.getValue();
                    nextTokenType = this.tokenizer.nextToken();
                    if (nextTokenType == '>')
                        return new WSWRLToken(WSWRLToken.WSWRLTokenType.IRI, iri);
                    else if (nextTokenType == StreamTokenizer.TT_EOF)
                        throw generateEndOfRuleException("Expecting '>' after IRI");
                    else
                        throw new WSWRLParseException("Expecting IRI after '<'");
                } else if (nextTokenType == StreamTokenizer.TT_EOF)
                    throw generateEndOfRuleException("Expecting IRI after '<'");
                else
                    throw new WSWRLParseException("Expecting IRI after '<'"); // Some other token
            }
            default:
                throw new WSWRLParseException(
                        "Unexpected character '" + String.valueOf(Character.toChars(tokenType)) + "'");
        }

    }

    @NonNull
    private WSWRLParseException generateEndOfRuleException(@NonNull String message) {
        if (!this.isInteractiveParseOnly())
            return new WSWRLParseException(message);
        else
            return new WSWRLIncompleteRuleException(message);
    }

    private boolean isInteger(@NonNull String s) {
        try {
            new BigInteger(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private class MyStreamTokenizer extends StreamTokenizer {
        public MyStreamTokenizer(@NonNull Reader r) {
            super(r);
        }

        @Override
        public void parseNumbers() {
        }

        @NonNull
        String getValue() {
            if (super.sval != null)
                return super.sval;
            else
                return "";
        }
    }
}
