package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class NumConst extends Expr {
    private final String value;

    private NumConst(String value) {
        super(Phrase.Kind.NumConst);
        this.value = value;
        setReadOnly();
    }

    public static NumConst make(String value) {
        return new NumConst(value);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Int[" + this.value + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append(this.value);
    }
}
