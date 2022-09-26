package de.fhg.iais.roberta.syntax.action.motor;

import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "ACTOR", blocklyNames = {"robActions_motor_getPower"}, name = "MOTOR_GET_POWER_ACTION", blocklyType = BlocklyType.NUMBER_INT)
public final class MotorGetPowerAction extends MoveAction {

    @NepoField(name = BlocklyConstants.MOTORPORT)
    public final String port;

    public MotorGetPowerAction(BlocklyProperties properties, String port) {
        super(properties, port);
        Assert.isTrue(port != null);
        this.port = port;
        setReadOnly();
    }
}
