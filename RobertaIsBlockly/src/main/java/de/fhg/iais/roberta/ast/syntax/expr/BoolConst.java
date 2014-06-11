package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class BoolConst extends Expr {
    private final boolean value;

    private BoolConst(boolean value) {
        super(Phrase.Kind.BoolConst);
        this.value = value;
        setReadOnly();
    }

    public static BoolConst make(boolean value) {
        return new BoolConst(value);
    }

    public boolean isValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "BoolConst [" + this.value + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append(this.value);
    }

}
