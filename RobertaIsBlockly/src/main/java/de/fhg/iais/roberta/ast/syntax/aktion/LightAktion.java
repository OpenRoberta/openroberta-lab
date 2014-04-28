package de.fhg.iais.roberta.ast.syntax.aktion;

import de.fhg.iais.roberta.dbc.Assert;

public class LightAktion extends Aktion {
    private final boolean turnOn;
    private final Co color;
    private final boolean blink;

    private LightAktion(boolean turnOn, Co color, boolean blink) {
        Assert.isTrue(color != null);
        this.turnOn = turnOn;
        this.color = color;
        this.blink = blink;
        setReadOnly();
    }

    public LightAktion make(boolean turnOn, Co color, boolean blink) {
        return new LightAktion(turnOn, color, blink);
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
    public Kind getKind() {
        return Kind.LightAktion;
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
