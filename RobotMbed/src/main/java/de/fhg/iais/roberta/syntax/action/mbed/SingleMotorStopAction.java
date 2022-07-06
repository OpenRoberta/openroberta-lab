package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "SINGLE_MOTOR_STOP_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_single_motor_stop"})
public final class SingleMotorStopAction extends Action {
    @NepoField(name = "MODE")
    public final MotorStopMode mode;

    public SingleMotorStopAction(BlocklyProperties properties, MotorStopMode mode) {
        super(properties);
        Assert.isTrue(mode != null);
        this.mode = mode;
        setReadOnly();
    }

}
