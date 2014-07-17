package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class StopAction extends Action {

    private StopAction() {
        super(Phrase.Kind.StopAction);
        setReadOnly();
    }

    public static StopAction make() {
        return new StopAction();
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "StopAction []";
    }

}
