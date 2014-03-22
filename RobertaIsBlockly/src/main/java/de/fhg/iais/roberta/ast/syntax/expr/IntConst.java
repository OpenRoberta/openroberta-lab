package de.fhg.iais.roberta.ast.syntax.expr;

public class IntConst extends Expr {
    private final int value;

    private IntConst(int value) {
        this.value = value;
        setReadOnly();
    }

    public static IntConst make(int value) {
        return new IntConst(value);
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public Kind getKind() {
        return Expr.Kind.IntConst;
    }

    @Override
    public String toString() {
        return "Int[" + this.value + "]";
    }
}
