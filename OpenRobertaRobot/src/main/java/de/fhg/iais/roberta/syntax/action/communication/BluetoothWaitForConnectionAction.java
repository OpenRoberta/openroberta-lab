package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robCommunication_waitForConnection"}, name = "BLUETOOTH_WAIT_FOR_CONNECTION_ACTION")
public final class BluetoothWaitForConnectionAction<V> extends Action<V> {

    public BluetoothWaitForConnectionAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        setReadOnly();
    }
    
    public static <V> BluetoothWaitForConnectionAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothWaitForConnectionAction<V>(properties, comment);
    }

}
