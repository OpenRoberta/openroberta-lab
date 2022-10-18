package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motordiff_on_for"}, name = "MOTORDIFF_ON_FOR_ACTION")
public final class MotorDiffOnForAction extends ActionWithoutUserChosenName {
    @NepoField(name = BlocklyConstants.DIRECTION)
    public final String direction;
    @NepoValue(name = BlocklyConstants.POWER, type = BlocklyType.NUMBER)
    public final Expr power;
    @NepoValue(name = "DISTANCE")
    public final Expr distance;

    public MotorDiffOnForAction(
        BlocklyProperties properties,
        String direction,
        Expr power,
        Expr distance, Hide hide) {
        super(properties, hide);
        this.direction = direction;
        this.power = power;
        this.distance = distance;
        setReadOnly();
    }

}