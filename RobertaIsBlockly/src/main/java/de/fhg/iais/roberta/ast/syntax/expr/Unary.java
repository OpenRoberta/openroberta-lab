package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class Unary extends Expr {
    private final Op op;
    private final Expr expr;

    private Unary(Op op, Expr expr) {
        Assert.isTrue(op != null && expr != null && expr.isReadOnly());
        this.op = op;
        this.expr = expr;
        setReadOnly();
    }

    public static Unary make(Op op, Expr expr) {
        return new Unary(op, expr);
    }

    public Op getOp() {
        return this.op;
    }

    public Expr getExpr() {
        return this.expr;
    }

    @Override
    public Kind getKind() {
        return Expr.Kind.Unary;
    }

    @Override
    public String toString() {
        return "Unary[" + this.op + ", " + this.expr + "]";
    }

    public static enum Op {
        Plus, Minus, Not;

        public static Op get(String s) {
            if ( "+".equals(s) ) {
                return Plus;
            } else if ( "-".equals(s) ) {
                return Minus;
            } else if ( "!".equals(s) ) {
                return Not;
            } else {
                throw new DbcException("invalid unary operator: " + s);
            }
        }
    }

}
