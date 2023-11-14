package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "OMNIDIFF_CURVE_ACTION", category = "ACTOR", blocklyNames = {"actions_motorOmniDiff_curve"})
public final class MotorOmniDiffCurveAction extends ActionWithoutUserChosenName {
    @NepoField(name = BlocklyConstants.DIRECTION)
    public final String direction;
    @NepoValue(name = BlocklyConstants.POWER_LEFT, type = BlocklyType.NUMBER)
    public final Expr powerLeft;
    @NepoValue(name = BlocklyConstants.POWER_RIGHT, type = BlocklyType.NUMBER)
    public final Expr powerRight;

    public MotorOmniDiffCurveAction(
        BlocklyProperties properties,
        String direction,
        Expr powerLeft,
        Expr powerRight,
        Hide hide) {
        super(properties, hide);
        this.direction = direction;
        this.powerLeft = powerLeft;
        this.powerRight = powerRight;
        setReadOnly();
    }

}