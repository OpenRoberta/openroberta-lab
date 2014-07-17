package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class MotorStopAction extends Action {
    private final ActorPort port;
    private final MotorStopMode mode;

    private MotorStopAction(ActorPort port, MotorStopMode mode) {
        super(Phrase.Kind.MotorStopAction);
        Assert.isTrue(port != null && mode != null);
        this.port = port;
        this.mode = mode;
        setReadOnly();
    }

    public static MotorStopAction make(ActorPort port, MotorStopMode mode) {
        return new MotorStopAction(port, mode);
    }

    public ActorPort getPort() {
        return this.port;
    }

    public MotorStopMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "MotorStop [port=" + this.port + ", mode=" + this.mode + "]";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

}
