package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_play_tone</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for making sound.<br/>
 * <br/>
 * The client must provide the frequency and the duration of the sound.
 */
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

    /**
     * Creates instance of {@link ToneAction}. This instance is read only and can not be modified.
     * 
     * @param frequency of the sound,
     * @param duration of the sound,
     * @return read only object of class {@link ToneAction}.
     */
    public static ToneAction make(Expr frequency, Expr duration) {
        return new ToneAction(frequency, duration);
    }

    /**
     * @return frequency of the sound
     */
    public Expr getFrequency() {
        return this.frequency;
    }

    /**
     * @return duration of the sound.
     */
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
