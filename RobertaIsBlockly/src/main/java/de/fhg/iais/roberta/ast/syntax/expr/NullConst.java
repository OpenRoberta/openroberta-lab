package de.fhg.iais.roberta.ast.syntax.expr;

public class NullConst extends Expr {

    private NullConst() {
        setReadOnly();
    }

    public static NullConst make() {
        return new NullConst();
    }

    public Object getValue() {
        return null;
    }

    @Override
    public Kind getKind() {
        return Expr.Kind.NullConst;
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
