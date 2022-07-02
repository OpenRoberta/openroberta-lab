package de.fhg.iais.roberta.syntax.action.speech;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_sayText_parameters"}, name = "SAY_TEXT_WITH_SPEED_AND_PITCH")
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

}
