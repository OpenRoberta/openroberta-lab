package de.fhg.iais.roberta.ast.syntax.action;

public class ClearDisplayAction extends Action {

    private ClearDisplayAction() {
        super(Kind.ClearDisplayAction);
        setReadOnly();
    }

    public static ClearDisplayAction make() {
        return new ClearDisplayAction();
    }

    @Override
    public String toString() {
        return "ClearDisplayAction []";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

}
