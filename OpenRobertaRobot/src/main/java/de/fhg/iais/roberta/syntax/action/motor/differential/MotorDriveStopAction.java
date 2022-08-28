package de.fhg.iais.roberta.syntax.action.motor.differential;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_motorDiff_stop"}, name = "STOP_ACTION")
public final class MotorDriveStopAction extends Action {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public MotorDriveStopAction(BlocklyProperties properties, String port, Hide hide) {
        super(properties);
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

}
