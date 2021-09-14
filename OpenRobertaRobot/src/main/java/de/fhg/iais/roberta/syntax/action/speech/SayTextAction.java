package de.fhg.iais.roberta.syntax.action.speech;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>naoActions_sayText</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * making the robot say the text.<br>
 * <br>
 * <br>
 */
@NepoPhrase(containerType = "SAY_TEXT")
public class SayTextAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.STRING)
    public final Expr<V> msg;
    @NepoValue(name = BlocklyConstants.VOICESPEED, type = BlocklyType.NUMBER_INT)
    public final Expr<V> speed;
    @NepoValue(name = BlocklyConstants.VOICEPITCH, type = BlocklyType.NUMBER_INT)
    public final Expr<V> pitch;

    public SayTextAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> msg, Expr<V> speed, Expr<V> pitch) {
        super(kind, properties, comment);
        Assert.isTrue(msg != null && speed != null && pitch != null);
        this.msg = msg;
        this.speed = speed;
        this.pitch = pitch;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SayTextAction}. This instance is read only and can not be modified.
     *
     * @param msg {@link Expr} that will be printed on the display of the brick; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SayTextAction}
     */
    public static <V> SayTextAction<V> make(Expr<V> msg, Expr<V> speed, Expr<V> pitch, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SayTextAction<>(BlockTypeContainer.getByName("SAY_TEXT"), properties, comment, msg, speed, pitch);
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
