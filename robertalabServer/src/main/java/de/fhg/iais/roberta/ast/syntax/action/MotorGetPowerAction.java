package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class MotorGetPowerAction extends Action {
    private final ActorPort port;

    private MotorGetPowerAction(ActorPort port) {
        super(Phrase.Kind.MotorGetPowerAction);
        Assert.isTrue(port != null);
        this.port = port;
        setReadOnly();
    }

    public static MotorGetPowerAction make(ActorPort port) {
        return new MotorGetPowerAction(port);
    }

    public ActorPort getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "MotorGetPower [port=" + this.port + "]";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }
}
