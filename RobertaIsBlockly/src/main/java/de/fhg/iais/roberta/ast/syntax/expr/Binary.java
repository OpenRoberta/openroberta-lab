package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

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

    public static Binary make(Op op, Expr left, Expr right) {
        return new Binary(op, left, right);
    }

    public Op getOp() {
        return this.op;
    }

    public Expr getLeft() {
        return this.left;
    }

    public Expr getRight() {
        return this.right;
    }

    @Override
    public String toString() {
        return "Binary [" + this.op + ", " + this.left + ", " + this.right + "]";
    }

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
        sb.append("(");
        this.left.toStringBuilder(sb, indentation);
        sb.append(" ").append(this.op).append(" ");
        this.right.toStringBuilder(sb, indentation);
        sb.append(")");
    }

}
