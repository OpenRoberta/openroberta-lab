package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class StringConst extends Expr {
    private final String value;

    private StringConst(String value) {
        super(Phrase.Kind.StringConst);
        this.value = value;
        setReadOnly();
    }

    public static StringConst make(String value) {
        return new StringConst(value);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "String [" + this.value + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append(this.value);
    }
}
