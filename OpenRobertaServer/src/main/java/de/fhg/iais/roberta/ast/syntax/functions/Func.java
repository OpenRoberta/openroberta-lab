package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents all functions from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(Function, List)}.<br>
 * The enumeration {@link Function} contains all allowed functions.
 */
public class Func<V> extends Expr<V> {
    private final Function functName;
    private final List<Expr<V>> param;

    private Func(Function name, List<Expr<V>> param) {
        super(Phrase.Kind.FUNCTIONS);
        Assert.isTrue(name != null && param != null);
        this.functName = name;
        this.param = param;

        setReadOnly();
    }

    /**
     * Creates instance of {@link Binary}. This instance is read only and can not be modified.
     * 
     * @param name of the function
     * @param param list of parameters for the function,
     * @return read only object of class {@link Func}
     */
    public static <V> Func<V> make(Function name, List<Expr<V>> param) {
        return new Func<V>(name, param);
    }

    /**
     * @return name of the function
     */
    public Function getFunctName() {
        return this.functName;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    @Override
    public int getPrecedence() {
        return this.functName.getPrecedence();
    }

    @Override
    public String toString() {
        return "Funct [" + this.functName + ", " + this.param + "]";
    }

    @Override
    public Assoc getAssoc() {
        return this.functName.getAssoc();
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitFunc(this);
    }

    /**
     * Function names.
     */
    public static enum Function {
        DIVISIBLE_BY( 80, Assoc.NONE ),
        MAX( 80, Assoc.NONE ),
        MIN( 80, Assoc.NONE ),
        FIRST( 1, Assoc.LEFT ),
        LAST( 1, Assoc.LEFT ),
        FROM_START( 1, Assoc.LEFT, "FROMSTART" ),
        FROM_END( 1, Assoc.LEFT, "FROMEND" ),
        LISTS_REPEAT( 1, Assoc.LEFT ),
        RANDOM_INTEGER( 1, Assoc.LEFT, "RANDOMINTEGER" ),
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
        CONSTRAIN( 10, Assoc.NONE ),
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
        TEXT_JOIN( 10, Assoc.LEFT ),
        TEXT_LENGTH( 10, Assoc.LEFT ),
        IS_EMPTY( 10, Assoc.LEFT ),
        UPPERCASE( 10, Assoc.LEFT ),
        LOWERCASE( 10, Assoc.LEFT ),
        TITLECASE( 10, Assoc.LEFT ),
        LEFT( 10, Assoc.LEFT ),
        RIGHT( 10, Assoc.LEFT ),
        BOTH( 10, Assoc.LEFT ),
        TEXT_PROMPT( 10, Assoc.LEFT, "TEXT" ),
        NUMBER_PROMPT( 10, Assoc.LEFT, "NUMBER" ),
        LISTS_LENGTH( 10, Assoc.LEFT ),
        SUBSTRING( 10, Assoc.LEFT ),
        PRINT( 10, Assoc.LEFT );

        private final String[] values;
        private final int precedence;
        private final Assoc assoc;

        private Function(int precedence, Assoc assoc, String... values) {
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
         * get function from {@link Function} from string parameter. It is possible for one function to have multiple string mappings.
         * Throws exception if the operator does not exists.
         * 
         * @param functName of the function
         * @return function from the enum {@link Function}
         */
        public static Function get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid function name: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Function funct : Function.values() ) {
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
}
