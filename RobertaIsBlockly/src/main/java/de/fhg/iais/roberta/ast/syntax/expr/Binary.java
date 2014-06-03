package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class Binary extends Expr {
    private final Op op;
    private final Expr left;
    private final Expr right;

    private Binary(Op op, Expr left, Expr right) {
        Assert.isTrue(op != null && left != null && right != null && left.isReadOnly() && right.isReadOnly());
        this.op = op;
        this.left = left;
        this.right = right;
        setReadOnly();
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
    public Kind getKind() {
        return Expr.Kind.Binary;
    }

    @Override
    public String toString() {
        return "Binary[" + this.op + ", " + this.left + ", " + this.right + "]";
    }

    public static enum Op {
        MULTIPLY,
        DIVIDE,
        ADD,
        MINUS,
        EQ,
        AND,
        OR,
        LT,
        LTE,
        GT,
        GTE,
        NEQ,
        POWER,
        DIVISIBLE_BY,
        MATH_CHANGE,
        MOD,
        MAX,
        MIN,
        RANDOM_INTEGER,
        TEXT_APPEND,
        FIRST,
        LAST,
        FROM_START,
        FROM_END,
        RANDOM,
        LISTS_REPEAT,
        ASSIGNMENT;

        public static Op get(String s) {
            if ( "*".equals(s) ) {
                return MULTIPLY;
            } else if ( "/".equals(s) ) {
                return DIVIDE;
            } else if ( "+".equals(s) ) {
                return ADD;
            } else if ( "-".equals(s) ) {
                return MINUS;
            } else if ( "==".equals(s) ) {
                return EQ;
            } else if ( "&&".equals(s) ) {
                return AND;
            } else if ( "||".equals(s) ) {
                return OR;
            } else if ( "<=".equals(s) ) {
                return LTE;
            } else if ( "=>".equals(s) ) {
                return GTE;
            } else if ( "<".equals(s) ) {
                return LT;
            } else if ( ">".equals(s) ) {
                return GT;
            } else if ( "!=".equals(s) ) {
                return NEQ;
            } else if ( "^".equals(s) ) {
                return POWER;
            } else if ( "divisible_by".equals(s) ) {
                return DIVISIBLE_BY;
            } else if ( "math_change".equals(s) ) {
                return MATH_CHANGE;
            } else if ( "mod".equals(s) ) {
                return MOD;
            } else if ( "max".equals(s) ) {
                return MAX;
            } else if ( "min".equals(s) ) {
                return MIN;
            } else if ( "randomInteger".equals(s) ) {
                return RANDOM_INTEGER;
            } else if ( "textAppend".equals(s) ) {
                return TEXT_APPEND;
            } else if ( "first".equals(s) ) {
                return FIRST;
            } else if ( "last".equals(s) ) {
                return LAST;
            } else if ( "fromStart".equals(s) ) {
                return FROM_START;
            } else if ( "fromEnd".equals(s) ) {
                return FROM_END;
            } else if ( "random".equals(s) ) {
                return RANDOM;
            } else if ( "lists_repeat".equals(s) ) {
                return LISTS_REPEAT;
            } else if ( "=".equals(s) ) {
                return ASSIGNMENT;
            } else {
                throw new DbcException("invalid binary operator: " + s);
            }
        }
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

}
