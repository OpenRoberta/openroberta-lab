package de.fhg.iais.roberta.ast.syntax.expr;

public class Var extends Expr {
    private final String name;

    private Var(String value) {
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
    public Kind getKind() {
        return Expr.Kind.Var;
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
