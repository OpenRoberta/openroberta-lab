package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class LightStatusAction extends Action {
    private final Status status;

    private LightStatusAction(Status status) {
        super(Phrase.Kind.LightStatusAction);
        Assert.isTrue(status != null);
        this.status = status;
        setReadOnly();
    }

    public static LightStatusAction make(Status status) {
        return new LightStatusAction(status);
    }

    public Status getStatus() {
        return this.status;
    }

    public static enum Status {
        OFF, RESET;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "LightStatusAction [" + this.status + "]";
    }
}
