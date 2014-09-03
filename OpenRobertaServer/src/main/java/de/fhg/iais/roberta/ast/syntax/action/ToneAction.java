package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_play_tone</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for making sound.<br/>
 * <br/>
 * The client must provide the frequency and the duration of the sound.
 */
public class ToneAction<V> extends Action<V> {
    private final Expr<V> frequency;
    private final Expr<V> duration;

    private ToneAction(Expr<V> frequency, Expr<V> duration, boolean disabled, String comment) {
        super(Phrase.Kind.TONE_ACTION, disabled, comment);
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
     * @param disabled state of the block,
     * @param comment added from the user
     * @return read only object of class {@link ToneAction}.
     */
    public static <V> ToneAction<V> make(Expr<V> frequency, Expr<V> duration, boolean disabled, String comment) {
        return new ToneAction<V>(frequency, duration, disabled, comment);
    }

    /**
     * @return frequency of the sound
     */
    public Expr<V> getFrequency() {
        return this.frequency;
    }

    /**
     * @return duration of the sound.
     */
    public Expr<V> getDuration() {
        return this.duration;
    }

    @Override
    public String toString() {
        return "ToneAction [" + this.frequency + ", " + this.duration + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitToneAction(this);
    }
}
