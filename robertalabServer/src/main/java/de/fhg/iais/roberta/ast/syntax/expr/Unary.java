package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * class for implementing all unary operations. To create an instance from this class use the method {@link #make(Op, Expr)}.<br>
 * The enumeration {@link Op} contains all allowed unary operations.
 * 
 * @author kcvejoski
 */
public class Unary extends Expr {
    private final Op op;
    private final Expr expr;

    private Unary(Op op, Expr expr) {
        super(Phrase.Kind.Unary);
        Assert.isTrue(op != null && expr != null && expr.isReadOnly());
        this.op = op;
        this.expr = expr;
        setReadOnly();
    }

    /**
     * creates instance of {@link Unary}. This instance is read only and can not be modified.
     * 
     * @param op operator
     * @param expr expression over which the operation is performed
     * @return Unary expression
     */
    public static Unary make(Op op, Expr expr) {
        return new Unary(op, expr);
    }

    /**
     * @return the operation in the binary expression. See enum {@link Op} for all possible operations.
     */
    public Op getOp() {
        return this.op;
    }

    /**
     * @return the expression on which the operation is performed. Returns subclass of {@link Expr}.
     */
    public Expr getExpr() {
        return this.expr;
    }

    @Override
    public String toString() {
        return "Unary [" + this.op + ", " + this.expr + "]";
    }

    /**
     * Operators for the unary expression.
     * 
     * @author kcvejoski
     */
    public static enum Op {
        PLUS( "+" ),
        NEG( "-" ),
        NOT( "!" ),
        ROOT( "SQRT" ),
        ABS(),
        LN(),
        LOG10(),
        EXP(),
        POW10(),
        SIN(),
        COS(),
        TAN(),
        ASIN(),
        ACOS(),
        ATAN(),
        EVEN(),
        ODD(),
        PRIME(),
        WHOLE(),
        POSITIVE(),
        NEGATIVE(),
        ROUND(),
        ROUNDUP( "CEIL" ),
        ROUNDDOWN( "FLOOR" ),
        SUM(),
        MIN(),
        MAX(),
        AVERAGE(),
        MEDIAN(),
        MODE(),
        STD_DEV(),
        RANDOM(),
        TEXT_JOIN(),
        TEXT_LENGTH(),
        IS_EMPTY(),
        UPPERCASE(),
        LOWERCASE(),
        TITLECASE(),
        LEFT(),
        RIGHT(),
        BOTH(),
        TEXT(),
        NUMBER(),
        LISTS_LENGTH();

        private final String[] values;

        private Op(String... values) {
            this.values = values;
        }

        /**
         * get operator from {@link Op} from string parameter. It is possible for one operator to have multiple string mappings.
         * Throws exception if the operator does not exists.
         * 
         * @param name of the operator
         * @return operator from the enum {@link Op}
         */
        public static Op get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid unary operator symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Op op : Op.values() ) {
                if ( op.toString().equals(sUpper) ) {
                    return op;
                }
                for ( String value : op.values ) {
                    if ( sUpper.equals(value) ) {
                        return op;
                    }
                }
            }
            throw new DbcException("Invalid unary operator symbol: " + s);
        }
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("(");
        sb.append(this.op).append(" (");
        this.expr.toStringBuilder(sb, indentation);
        sb.append("))");

    }

}
