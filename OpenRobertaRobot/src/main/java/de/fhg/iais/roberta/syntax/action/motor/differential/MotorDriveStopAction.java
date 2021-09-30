package de.fhg.iais.roberta.syntax.action.motor.differential;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;

/**
 * This class represents the <b>robActions_motorDiff_stop</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * to stop the work of the motors.<br/>
 */
@NepoPhrase(containerType = "STOP_ACTION")
public class MotorDriveStopAction<V> extends Action<V> {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public MotorDriveStopAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String port, Hide hide) {
        super(kind, properties, comment);
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorDriveStopAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorDriveStopAction}
     */
    public static <V> MotorDriveStopAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotorDriveStopAction<>(BlockTypeContainer.getByName("STOP_ACTION"), properties, comment, BlocklyConstants.EMPTY_PORT, null);
    }

    public static <V> MotorDriveStopAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment, String port, Hide hide) {
        return new MotorDriveStopAction<>(BlockTypeContainer.getByName("STOP_ACTION"), properties, comment, port, hide);
    }
}
