package com.sebastienguillemin.wswrl.parser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * Defines a basic token class used by the
 * {@link com.sebastienguillemin.wswrl.parser.WSWRLToken} and
 * {@link com.sebastienguillemin.wswrl.parser.WSWRLTokenizer}.
 * 
 * <br><br>
 * 
 * Code adapted from the SWRL API.
 *
 * @see com.sebastienguillemin.wswrl.parser.WSWRLToken
 * @see com.sebastienguillemin.wswrl.parser.WSWRLTokenizer
 */
class WSWRLToken {
    @NonNull
    private final WSWRLTokenType tokenType;
    @NonNull
    private final String value;

    public WSWRLToken(@NonNull WSWRLTokenType tokenType, @NonNull String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    @NonNull
    public WSWRLTokenType getTokenType() {
        return this.tokenType;
    }

    @NonNull
    public String getValue() {
        return this.value;
    }

    @NonNull
    public int getIntegerValue() {
        return Integer.parseInt(this.value);
    }

    public boolean isImp() {
        return this.tokenType == WSWRLTokenType.IMP;
    }

    public boolean isRing() {
        return this.tokenType == WSWRLTokenType.RING;
    }

    public boolean isConjunction() {
        return this.tokenType == WSWRLTokenType.CONJUNCTION;
    }

    public boolean isString() {
        return this.tokenType == WSWRLTokenType.STRING;
    }

    public boolean isShortName() {
        return this.tokenType == WSWRLTokenType.SHORTNAME;
    }

    public boolean isIRI() {
        return this.tokenType == WSWRLTokenType.IRI;
    }

    public boolean isInteger() {
        return this.tokenType == WSWRLTokenType.INTEGER;
    }

    public boolean isDecimal() {
        return this.tokenType == WSWRLTokenType.DECIMAL;
    }

    public boolean isTypeQualifier() {
        return this.tokenType == WSWRLTokenType.TYPE_QUAL;
    }

    public boolean isLParen() {
        return this.tokenType == WSWRLTokenType.LPAREN;
    }

    public boolean isRParen() {
        return this.tokenType == WSWRLTokenType.RPAREN;
    }

    public boolean isComma() {
        return this.tokenType == WSWRLTokenType.COMMA;
    }

    public boolean isQuestion() {
        return this.tokenType == WSWRLTokenType.QUESTION;
    }

    public boolean isAnd() {
        return this.tokenType == WSWRLTokenType.AND;
    }

    public boolean isOr() {
        return this.tokenType == WSWRLTokenType.OR;
    }

    public boolean isNot() {
        return this.tokenType == WSWRLTokenType.NOT;
    }

    public boolean isStar() {
        return this.tokenType == WSWRLTokenType.STAR;
    }

    public boolean isRank() {
        return this.tokenType == WSWRLTokenType.RANK;
    }

    public boolean isEndOfInput() {
        return this.tokenType == WSWRLTokenType.END_OF_INPUT;
    }

    @NonNull
    @SideEffectFree
    @Override
    public String toString() {
        return "[" + this.tokenType.getName() + " with value '" + this.value + "']";
    }

    public enum WSWRLTokenType {
        // A short name is a user-friendly name. Note: it can be a prefixed name or a full IRI.
        SHORTNAME("short name"),
        IRI("IRI"),
        STRING("quoted string"),
        DECIMAL("decimal"),
        INTEGER("integer"),
        TYPE_QUAL("^^"),
        CONJUNCTION("^"),
        IMP("->"),
        RING("."),
        LPAREN("("),
        RPAREN(")"),
        COMMA(","),
        QUESTION("?"),
        AND("AND"),
        OR("OR"),
        NOT("NOT"),
        STAR("STAR"),
        RANK("RANK"),
        END_OF_INPUT("end");

        @NonNull
        private final String name;

        WSWRLTokenType(@NonNull String name) {
            this.name = name;
        }

        @NonNull
        public String getName() {
            return this.name;
        }
    }
}
