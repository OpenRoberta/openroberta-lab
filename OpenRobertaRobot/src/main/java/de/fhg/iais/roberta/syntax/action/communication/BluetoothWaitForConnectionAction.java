package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.transformer.NepoPhrase;

@NepoPhrase(containerType = "BLUETOOTH_WAIT_FOR_CONNECTION_ACTION")
public class BluetoothWaitForConnectionAction<V> extends Action<V> {

    public BluetoothWaitForConnectionAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorDriveStopAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorDriveStopAction}
     */
    public static <V> BluetoothWaitForConnectionAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothWaitForConnectionAction<V>(BlockTypeContainer.getByName("BLUETOOTH_WAIT_FOR_CONNECTION_ACTION"), properties, comment);
    }

}
