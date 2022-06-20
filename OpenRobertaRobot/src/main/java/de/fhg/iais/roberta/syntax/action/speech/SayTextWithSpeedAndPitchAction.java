package de.fhg.iais.roberta.syntax.action.speech;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_sayText_parameters"}, containerType = "SAY_TEXT_WITH_SPEED_AND_PITCH")
public final class SayTextWithSpeedAndPitchAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.STRING)
    public final Expr<V> msg;
    @NepoValue(name = BlocklyConstants.VOICESPEED, type = BlocklyType.NUMBER_INT)
    public final Expr<V> speed;
    @NepoValue(name = BlocklyConstants.VOICEPITCH, type = BlocklyType.NUMBER_INT)
    public final Expr<V> pitch;

    public SayTextWithSpeedAndPitchAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> msg, Expr<V> speed, Expr<V> pitch) {
        super(properties, comment);
        Assert.isTrue(msg != null && speed != null && pitch != null);
        this.msg = msg;
        this.speed = speed;
        this.pitch = pitch;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SayTextWithSpeedAndPitchAction}. This instance is read only and can not be modified.
     *
     * @param msg {@link Expr} that will be printed on the display of the brick; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SayTextWithSpeedAndPitchAction}
     */
    public static <V> SayTextWithSpeedAndPitchAction<V> make(Expr<V> msg, Expr<V> speed, Expr<V> pitch, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SayTextWithSpeedAndPitchAction<>(properties, comment, msg, speed, pitch);
    }

    /**
     * @return the message.
     */
    public Expr<V> getMsg() {
        return this.msg;
    }

    /**
     * @return the pitch.
     */
    public Expr<V> getPitch() {
        return this.pitch;
    }

    /**
     * @return the speed.
     */
    public Expr<V> getSpeed() {
        return this.speed;
    }

}
