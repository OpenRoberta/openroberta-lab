package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motordiff_curve_for"}, name = "MOTORDIFF_CURVE_FOR_ACTION")
public final class MotorDiffCurveForAction extends ActionWithoutUserChosenName {
    @NepoField(name = BlocklyConstants.DIRECTION)
    public final String direction;
    @NepoValue(name = BlocklyConstants.POWER_LEFT, type = BlocklyType.NUMBER)
    public final Expr powerLeft;
    @NepoValue(name = BlocklyConstants.POWER_RIGHT, type = BlocklyType.NUMBER)
    public final Expr powerRight;
    @NepoValue(name = "DISTANCE")
    public final Expr distance;

    public MotorDiffCurveForAction(
        BlocklyProperties properties,
        String direction,
        Expr powerLeft,
        Expr powerRight,
        Expr distance,
        Hide hide) {
        super(properties, hide);
        this.direction = direction;
        this.powerLeft = powerLeft;
        this.powerRight = powerRight;
        this.distance = distance;
        setReadOnly();
    }

}