package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "SINGLE_MOTOR_ON_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_single_motor_on"})
public final class SingleMotorOnAction extends Action {
    @NepoValue(name = "POWER", type = BlocklyType.NUMBER_INT)
    public final Expr speed;

    public SingleMotorOnAction(BlocklyProperties properties, Expr speed) {
        super(properties);
        Assert.isTrue((speed != null) && speed.isReadOnly());
        this.speed = speed;

        setReadOnly();
    }


}
