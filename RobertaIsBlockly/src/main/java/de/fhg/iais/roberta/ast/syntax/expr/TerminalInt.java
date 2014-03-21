package de.fhg.iais.roberta.ast.syntax.expr;

public class TerminalInt extends Expr {
    private final int value;

    private TerminalInt(int value) {
        this.value = value;
        setReadOnly();
    }

    public static TerminalInt make(int value) {
        return new TerminalInt(value);
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public Kind getKind() {
        return Expr.Kind.TerminalInt;
    }

    @Override
    public String toString() {
        return "Int[" + this.value + "]";
    }
}
