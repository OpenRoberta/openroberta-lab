package de.fhg.iais.roberta.syntax.action.motor;

import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_motor_setPower"}, name = "MOTOR_SET_POWER_ACTION")
public final class MotorSetPowerAction extends MoveAction {
    @NepoValue(name = BlocklyConstants.POWER, type = BlocklyType.NUMBER_INT)
    public final Expr power;
    @NepoField(name = BlocklyConstants.MOTORPORT)
    public final String port;

    public MotorSetPowerAction(BlocklyProperties properties, Expr power, String port) {
        super(properties, port);
        Assert.isTrue(port != null && power.isReadOnly());
        this.port = port;
        this.power = power;
        setReadOnly();
    }

}
