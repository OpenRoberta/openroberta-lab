package de.fhg.iais.roberta.ast.funct;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class Funct extends Expr {
    private final Function functName;
    private final List<Expr> param;

    private Funct(Function name, List<Expr> param) {
        super(Phrase.Kind.Funct);
        Assert.isTrue(name != null && param != null);
        this.functName = name;
        this.param = param;

        setReadOnly();
    }

    public static Funct make(Function name, List<Expr> param) {
        return new Funct(name, param);
    }

    public Function getFunctName() {
        return this.functName;
    }

    public List<Expr> getParam() {
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
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("Math.pow(");
        this.param.get(0).generateJava(sb, 0);
        sb.append(", ");
        this.param.get(1).generateJava(sb, 0);
        sb.append(")");
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

        public String getOpSymbol() {
            if ( this.values.length == 0 ) {
                return this.toString();
            } else {
                return this.values[0];
            }
        }

        public int getPrecedence() {
            return this.precedence;
        }

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
