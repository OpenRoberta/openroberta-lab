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
    public int getPrecedence() {
        return this.op.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.op.getAssoc();
    }

    @Override
    public String toString() {
        return "Binary [" + this.op + ", " + this.left + ", " + this.right + "]";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        generateSubExpr(sb, false, this.left);
        sb.append(" " + this.op.getOpSymbol() + " ");
        generateSubExpr(sb, this.op == Op.MINUS, this.right);
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr expr) {
        if ( expr.getPrecedence() >= this.getPrecedence() && !minusAdaption ) {
            // parentheses are omitted
            expr.generateJava(sb, 0);
        } else {
            sb.append("( ");
            expr.generateJava(sb, 0);
            sb.append(" )");
        }
    }

    /**
     * Operators for the binary expression.
     * 
     * @author kcvejoski
     */
    public static enum Op {
        ADD( 100, Assoc.LEFT, "+" ),
        MINUS( 100, Assoc.LEFT, "-" ),
        MULTIPLY( 200, Assoc.LEFT, "*" ),
        DIVIDE( 200, Assoc.LEFT, "/" ),
        MOD( 200, Assoc.NONE, "%" ),
        EQ( 80, Assoc.LEFT, "==" ),
        NEQ( 80, Assoc.LEFT, "!=", "<>" ),
        LT( 90, Assoc.LEFT, "<" ),
        LTE( 90, Assoc.LEFT, "<=" ),
        GT( 90, Assoc.LEFT, ">" ),
        GTE( 90, Assoc.LEFT, ">=" ),
        AND( 70, Assoc.LEFT, "&&" ),
        OR( 60, Assoc.LEFT, "||" ),
        MATH_CHANGE( 80, Assoc.NONE ),
        TEXT_APPEND( 1, Assoc.LEFT, "TEXTAPPEND" ),
        IN( 1, Assoc.LEFT, ":" ),
        ASSIGNMENT( 1, Assoc.RIGHT, "=" ),
        ADD_ASSIGNMENT( 1, Assoc.RIGHT, "+=" ),
        MINUS_ASSIGNMENT( 1, Assoc.RIGHT, "-=" ),
        MULTIPLY_ASSIGNMENT( 1, Assoc.RIGHT, "*=" ),
        DIVIDE_ASSIGNMENT( 1, Assoc.RIGHT, "/=" ),
        MOD_ASSIGNMENT( 1, Assoc.RIGHT, "%=" );

        private final String[] values;
        private final int precedence;
        private final Assoc assoc;

        private Op(int precedence, Assoc assoc, String... values) {
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

}
