package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motorOmni_stop_txt"}, name = "MOTOR_OMNI_STOP_ACTION")
public final class MotorOmniStopAction extends ActionWithoutUserChosenName {

    public MotorOmniStopAction(
        BlocklyProperties properties,
        Hide hide) {
        super(properties, hide);
        setReadOnly();
    }

}