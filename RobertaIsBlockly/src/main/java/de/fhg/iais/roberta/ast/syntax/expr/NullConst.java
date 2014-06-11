package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class NullConst extends Expr {

    private NullConst() {
        super(Phrase.Kind.NullConst);
        setReadOnly();
    }

    public static NullConst make() {
        return new NullConst();
    }

    public Object getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "NullConst [null]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

}
