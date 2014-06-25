package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class MotorOnAction extends Action {
    ActorPort port;
    MotionParam param;

    private MotorOnAction(ActorPort port, MotionParam param) {
        super(Phrase.Kind.MotorOnAction);
        Assert.isTrue(param != null);
        this.param = param;
        this.port = port;
        setReadOnly();
    }

    public static MotorOnAction make(ActorPort port, MotionParam param) {
        return new MotorOnAction(port, param);
    }

    public MotionParam getParam() {
        return this.param;
    }

    public ActorPort getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "MotorOnAction [" + this.port + ", " + this.param + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("(" + this.param + ", " + this.port + ")");
    }

}
