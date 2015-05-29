package de.fhg.iais.roberta.syntax.functions;

import java.util.Locale;

import de.fhg.iais.roberta.syntax.expr.Assoc;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum FunctionNames {
    DIVISIBLE_BY( 80, Assoc.NONE ),
    MAX( 80, Assoc.NONE ),
    MIN( 80, Assoc.NONE ),
    LISTS_REPEAT( 1, Assoc.LEFT ),
    RANDOM( 1, Assoc.LEFT ),
    EVEN( 10, Assoc.LEFT ),
    ODD( 10, Assoc.LEFT ),
    PRIME( 10, Assoc.LEFT ),
    WHOLE( 10, Assoc.LEFT ),
    POSITIVE( 10, Assoc.LEFT ),
    NEGATIVE( 10, Assoc.LEFT ),
    SUM( 10, Assoc.LEFT ),
    AVERAGE( 10, Assoc.LEFT ),
    MEDIAN( 10, Assoc.LEFT ),
    MODE( 10, Assoc.LEFT ),
    STD_DEV( 10, Assoc.LEFT ),
    ROOT( 10, Assoc.LEFT, "SQRT" ),
    ABS( 10, Assoc.LEFT ),
    LN( 10, Assoc.LEFT ),
    LOG10( 10, Assoc.LEFT ),
    EXP( 10, Assoc.LEFT ),
    POW10( 10, Assoc.LEFT ),
    SIN( 10, Assoc.LEFT ),
    COS( 10, Assoc.LEFT ),
    TAN( 10, Assoc.LEFT ),
    ASIN( 10, Assoc.LEFT ),
    ACOS( 10, Assoc.LEFT ),
    ATAN( 10, Assoc.LEFT ),
    POWER( 300, Assoc.RIGHT, "^" ),
    ROUND( 10, Assoc.LEFT ),
    ROUNDUP( 10, Assoc.LEFT, "CEIL" ),
    ROUNDDOWN( 10, Assoc.LEFT, "FLOOR" ),
    LIST_IS_EMPTY( 10, Assoc.LEFT, "LISTS_ISEMPTY" ),
    LEFT( 10, Assoc.LEFT ),
    RIGHT( 10, Assoc.LEFT ),
    TEXT( 10, Assoc.LEFT, "TEXT" ),
    NUMBER( 10, Assoc.LEFT, "NUMBER" ),
    LISTS_LENGTH( 10, Assoc.LEFT, "lists_length" ),
    GET_SUBLIST( 10, Assoc.LEFT );

    private final String[] values;
    private final int precedence;
    private final Assoc assoc;

    private FunctionNames(int precedence, Assoc assoc, String... values) {
        this.precedence = precedence;
        this.assoc = assoc;
        this.values = values;
    }

    /**
     * @return symbol of the function if it exists otherwise the name of the function.
     */
    public String getOpSymbol() {
        if ( this.values.length == 0 ) {
            return this.toString();
        } else {
            return this.values[0];
        }
    }

    /**
     * @return precedence of the operator
     */
    public int getPrecedence() {
        return this.precedence;
    }

    /**
     * @return association of the operator
     */
    public Assoc getAssoc() {
        return this.assoc;
    }

    /**
     * get function from {@link FunctionNames} from string parameter. It is possible for one function to have multiple string mappings.
     * Throws exception if the operator does not exists.
     *
     * @param functName of the function
     * @return function from the enum {@link FunctionNames}
     */
    public static FunctionNames get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid function name: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( FunctionNames funct : FunctionNames.values() ) {
            if ( funct.toString().equals(sUpper) ) {
                return funct;
            }
            for ( String value : funct.values ) {
                if ( sUpper.equals(value) ) {
                    return funct;
                }
            }
        }
        throw new DbcException("Invalid function name: " + s);
    }
}
