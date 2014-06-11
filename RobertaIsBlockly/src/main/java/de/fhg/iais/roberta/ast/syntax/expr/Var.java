package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class Var extends Expr {
    private final String name;

    private Var(String value) {
        super(Phrase.Kind.Var);
        this.name = value;
        setReadOnly();
    }

    public static Var make(String value) {
        return new Var(value);
    }

    public String getValue() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Var[" + this.name + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }
}
