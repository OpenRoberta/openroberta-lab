package de.fhg.iais.roberta.ast.syntax.aktion;

import de.fhg.iais.roberta.dbc.Assert;

public class ToneAktion extends Aktion {
    private final int frequency;
    private final int duration;

    private ToneAktion(int frequency, int duration) {
        Assert.isTrue(frequency > 0 && duration > 0);
        this.frequency = frequency;
        this.duration = duration;
        setReadOnly();
    }

    public ToneAktion make(int frequency, int duration) {
        return new ToneAktion(frequency, duration);
    }

    public int getFrequency() {
        return this.frequency;
    }

    public int getDuration() {
        return this.duration;
    }

    @Override
    public Kind getKind() {
        return Kind.ToneAktion;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "ToneAktion [" + this.frequency + ", " + this.duration + "]";
    }
}
