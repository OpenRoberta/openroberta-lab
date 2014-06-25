package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class TurnAction extends Action {
    private final Di direction;
    private final MotionParam param;

    private TurnAction(Di direction, MotionParam param) {
        super(Phrase.Kind.TurnAktion);
        Assert.isTrue(direction != null && param != null);
        this.direction = direction;
        this.param = param;
        setReadOnly();
    }

    public static TurnAction make(Di direction, MotionParam param) {
        return new TurnAction(direction, param);
    }

    public Di getDirection() {
        return this.direction;
    }

    public MotionParam getParam() {
        return this.param;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "TurnAktion [" + this.direction + ", dist: " + this.param.getDuration() + ", speed: " + this.param.getSpeed() + "]";
    }

    public static enum Di {
        right, left;
    }
}
