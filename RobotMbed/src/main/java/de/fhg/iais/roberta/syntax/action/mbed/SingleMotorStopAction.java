package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "SINGLE_MOTOR_STOP_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_single_motor_stop"})
public final class SingleMotorStopAction<V> extends Action<V> {
    @NepoField(name = "MODE")
    public final MotorStopMode mode;

    public SingleMotorStopAction(BlocklyBlockProperties properties, BlocklyComment comment, MotorStopMode mode) {
        super(properties, comment);
        Assert.isTrue(mode != null);
        this.mode = mode;
        setReadOnly();
    }

}
