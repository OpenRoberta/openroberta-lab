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
        Mult, Div, Add, Sub, Equal, And, Or, Less, LessEqual, Bigger, BiggerEqual, NotEqual;

        public static Op get(String s) {
            if ( "*".equals(s) ) {
                return Mult;
            } else if ( "/".equals(s) ) {
                return Div;
            } else if ( "+".equals(s) ) {
                return Add;
            } else if ( "-".equals(s) ) {
                return Sub;
            } else if ( "==".equals(s) ) {
                return Equal;
            } else if ( "&&".equals(s) ) {
                return And;
            } else if ( "||".equals(s) ) {
                return Or;
            } else if ( "<=".equals(s) ) {
                return LessEqual;
            } else if ( "=>".equals(s) ) {
                return BiggerEqual;
            } else if ( "<".equals(s) ) {
                return Less;
            } else if ( ">".equals(s) ) {
                return Bigger;
            } else if ( "!=".equals(s) ) {
                return NotEqual;
            } else {
                throw new DbcException("invalid binary operator: " + s);
            }
        }
    }

}
