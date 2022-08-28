package de.fhg.iais.roberta.syntax.action.speech;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_sayText_parameters"}, name = "SAY_TEXT_WITH_SPEED_AND_PITCH")
public final class SayTextWithSpeedAndPitchAction extends Action {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.STRING)
    public final Expr msg;
    @NepoValue(name = BlocklyConstants.VOICESPEED, type = BlocklyType.NUMBER_INT)
    public final Expr speed;
    @NepoValue(name = BlocklyConstants.VOICEPITCH, type = BlocklyType.NUMBER_INT)
    public final Expr pitch;

    public SayTextWithSpeedAndPitchAction(BlocklyProperties properties, Expr msg, Expr speed, Expr pitch) {
        super(properties);
        Assert.isTrue(msg != null && speed != null && pitch != null);
        this.msg = msg;
        this.speed = speed;
        this.pitch = pitch;
        setReadOnly();
    }

}
