package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motor_stop_txt4"}, name = "MOTOR_STOP_ACTION")
public final class MotorStopAction extends ActionWithUserChosenName {
    public MotorStopAction(
        BlocklyProperties properties,
        String port) {
        super(properties, port);
        setReadOnly();
    }

}