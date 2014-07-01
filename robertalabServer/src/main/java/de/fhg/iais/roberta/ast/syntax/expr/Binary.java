package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * class for implementing all binary operations. To create an instance from this class use the method {@link #make(Op, Expr, Expr)}.<br>
 * The enumeration {@link Op} contains all allowed binary operations.
 * 
 * @author kcvejoski
 */
public class Binary extends Expr {
    private final Op op;
    private final Expr left;
    private final Expr right;

    private Binary(Op op, Expr left, Expr right) {
        super(Phrase.Kind.Binary);
        Assert.isTrue(op != null && left != null && right != null && left.isReadOnly() && right.isReadOnly());
        this.op = op;
        this.left = left;
        this.right = right;
        this.setReadOnly();
    }

    /**
     * creates instance of {@link Binary}. This instance is read only and can not be modified.
     * 
     * @param op operator
     * @param left expression on the left hand side,
     * @param right expression on the right hand side
     * @return Binary expression
     */
    public static Binary make(Op op, Expr left, Expr right) {
        return new Binary(op, left, right);
    }

    /**
     * @return the operation in the binary expression. See enum {@link Op} for all possible operations.
     */
    public Op getOp() {
        return this.op;
    }

    /**
     * @return the expression on the left hand side. Returns subclass of {@link Expr}.
     */
    public Expr getLeft() {
        return this.left;
    }

    /**
     * @return the expression on the right hand side. Returns subclass of {@link Expr}.
     */
    public Expr getRight() {
        return this.right;
    }

    @Override
    public String toString() {
        return "Binary [" + this.op + ", " + this.left + ", " + this.right + "]";
    }

    /**
     * Operators for the binary expression.
     * 
     * @author kcvejoski
     */
    public static enum Op {
        MULTIPLY( "*" ),
        DIVIDE( "/" ),
        ADD( "+" ),
        MINUS( "-" ),
        EQ( "==" ),
        AND( "&&" ),
        OR( "||" ),
        LT( "<" ),
        LTE( "<=" ),
        GT( ">" ),
        GTE( ">=" ),
        NEQ( "!=", "<>" ),
        POWER( "^" ),
        DIVISIBLE_BY(),
        MATH_CHANGE(),
        MOD(),
        MAX(),
        MIN(),
        RANDOM_INTEGER( "RANDOMINTEGER" ),
        TEXT_APPEND( "TEXTAPPEND" ),
        FIRST(),
        LAST(),
        FROM_START( "FROMSTART" ),
        FROM_END( "FROMEND" ),
        RANDOM(),
        LISTS_REPEAT(),
        ASSIGNMENT( "=" ),
        IN( ":" );

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
                throw new DbcException("Invalid binary operator symbol: " + s);
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
            throw new DbcException("Invalid binary operator symbol: " + s);
        }
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("((");
        this.left.toStringBuilder(sb, indentation);
        sb.append(") ").append(this.op).append(" (");
        this.right.toStringBuilder(sb, indentation);
        sb.append("))");
    }

}
