package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class ToneAction extends Action {
    private final int frequency;
    private final int duration;

    private ToneAction(int frequency, int duration) {
        super(Phrase.Kind.ToneAktion);
        Assert.isTrue(frequency > 0 && duration > 0);
        this.frequency = frequency;
        this.duration = duration;
        setReadOnly();
    }

    public ToneAction make(int frequency, int duration) {
        return new ToneAction(frequency, duration);
    }

    public int getFrequency() {
        return this.frequency;
    }

    public int getDuration() {
        return this.duration;
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
