package de.fhg.iais.roberta.ast.syntax.expr;

public class StringConst extends Expr {
    private final String value;

    private StringConst(String value) {
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
    public Kind getKind() {
        return Expr.Kind.StringConst;
    }

    @Override
    public String toString() {
        return "String[" + this.value + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }
}
