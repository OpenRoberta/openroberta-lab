package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

public class ToneAction extends Action {
    private final Expr frequency;
    private final Expr duration;

    private ToneAction(Expr frequency, Expr duration) {
        super(Phrase.Kind.ToneAction);
        Assert.isTrue(frequency.isReadOnly() && duration.isReadOnly() && frequency != null && duration != null);
        this.frequency = frequency;
        this.duration = duration;
        setReadOnly();
    }

    public static ToneAction make(Expr frequency, Expr duration) {
        return new ToneAction(frequency, duration);
    }

    public Expr getFrequency() {
        return this.frequency;
    }

    public Expr getDuration() {
        return this.duration;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "ToneAction [" + this.frequency + ", " + this.duration + "]";
    }
}
