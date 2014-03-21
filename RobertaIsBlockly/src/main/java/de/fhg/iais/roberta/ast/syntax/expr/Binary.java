package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.dbc.Assert;

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

    public static enum Op {
        Mult, Div, Add, Sub;
    }

}
