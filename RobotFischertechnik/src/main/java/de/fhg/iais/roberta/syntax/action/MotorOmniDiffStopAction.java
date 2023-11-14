package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motorOmniDiff_stop_txt4"}, name = "MOTOR_OMNIDIFF_STOP_ACTION")
public final class MotorOmniDiffStopAction extends ActionWithoutUserChosenName {

    public MotorOmniDiffStopAction(
        BlocklyProperties properties,
        Hide hide) {
        super(properties, hide);
        setReadOnly();
    }

}