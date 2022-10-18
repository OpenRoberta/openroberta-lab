package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motordiff_stop"}, name = "MOTOR_STOP_ACTION")
public final class MotorDiffStopAction extends ActionWithoutUserChosenName {
    @NepoField(name = "CONTROL")
    public final String control;

    public MotorDiffStopAction(
        BlocklyProperties properties,
        String control,
        Hide hide) {
        super(properties, hide);
        this.control = control;
        setReadOnly();
    }

}