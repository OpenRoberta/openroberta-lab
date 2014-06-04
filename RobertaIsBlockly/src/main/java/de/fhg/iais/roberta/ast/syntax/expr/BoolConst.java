package de.fhg.iais.roberta.ast.syntax.expr;

public class BoolConst extends Expr {
    private final boolean value;

    private BoolConst(boolean value) {
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
    public Kind getKind() {
        return Expr.Kind.BoolConst;
    }

    @Override
    public String toString() {
        return "BoolConst [" + this.value + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

}
