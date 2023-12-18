package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "OMNI_TURN_ACTION", category = "ACTOR", blocklyNames = {"actions_motorOmni_turn_for_txt"})
public final class MotorOmniTurnForAction extends ActionWithoutUserChosenName {
    @NepoField(name = BlocklyConstants.DIRECTION)
    public final String direction;
    @NepoValue(name = BlocklyConstants.POWER, type = BlocklyType.NUMBER)
    public final Expr power;
    @NepoValue(name = BlocklyConstants.DURATION, type = BlocklyType.NUMBER)
    public final Expr duration;

    public MotorOmniTurnForAction(
        BlocklyProperties properties,
        String direction,
        Expr power,
        Expr duration,
        Hide hide) {
        super(properties, hide);
        this.direction = direction;
        this.power = power;
        this.duration = duration;
        setReadOnly();
    }

}