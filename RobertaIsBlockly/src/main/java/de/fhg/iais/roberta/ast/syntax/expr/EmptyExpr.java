package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class EmptyExpr extends Expr {

    private final Class<?> defVal;

    private EmptyExpr(Class<?> defVal) {
        super(Phrase.Kind.EmptyExpr);
        Assert.isTrue(defVal != null);
        this.defVal = defVal;
        setReadOnly();
    }

    public static EmptyExpr make(Class<?> defVal) {
        return new EmptyExpr(defVal);
    }

    public Class<?> getDefVal() {
        return this.defVal;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("[[Nothing supplied from the user for this place]]");
    }

    @Override
    public String toString() {
        return "EmptyExpr [defVal=" + this.defVal + "]";
    }

}
