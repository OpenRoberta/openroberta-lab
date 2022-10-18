package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motor_stop"}, name = "MOTOR_STOP_ACTION")
public final class MotorStopAction extends ActionWithUserChosenName {
    @NepoField(name = "CONTROL")
    public final String control;

    public MotorStopAction(
        BlocklyProperties properties,
        String control,
        String port) {
        super(properties, port);
        this.control = control;
        setReadOnly();
    }

}