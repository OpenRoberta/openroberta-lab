package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class LightAction extends Action {
    private final boolean turnOn;
    private final Co color;
    private final boolean blink;

    private LightAction(boolean turnOn, Co color, boolean blink) {
        super(Phrase.Kind.LightAktion);
        Assert.isTrue(color != null);
        this.turnOn = turnOn;
        this.color = color;
        this.blink = blink;
        setReadOnly();
    }

    public LightAction make(boolean turnOn, Co color, boolean blink) {
        return new LightAction(turnOn, color, blink);
    }

    public boolean isTurnOn() {
        return this.turnOn;
    }

    public Co getColor() {
        return this.color;
    }

    public boolean isBlink() {
        return this.blink;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "LightAktion [" + this.turnOn + ", " + this.color + "," + this.blink + "]";
    }

    public static enum Co {
        green, orange, red;
    }
}
