package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class IntConst extends Expr {
    private final int value;

    private IntConst(int value) {
        super(Phrase.Kind.IntConst);
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
    public String toString() {
        return "Int[" + this.value + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }
}
