package de.fhg.iais.roberta.syntax.action.motor;

import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_motor_setPower"}, name = "MOTOR_SET_POWER_ACTION")
public final class MotorSetPowerAction<V> extends MoveAction<V> {
    @NepoValue(name = BlocklyConstants.POWER, type = BlocklyType.NUMBER_INT)
    public final Expr<V> power;
    @NepoField(name = BlocklyConstants.MOTORPORT)
    public final String port;

    public MotorSetPowerAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> power, String port) {
        super(properties, comment, port);
        Assert.isTrue(port != null && power.isReadOnly());
        this.port = port;
        this.power = power;
        setReadOnly();
    }

    public static <V> MotorSetPowerAction<V> make(String port, Expr<V> power, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotorSetPowerAction<V>(properties, comment, power, port);
    }

    /**
     * @return value of the power of the motor.
     */
    public Expr<V> getPower() {
        return this.power;
    }

}
