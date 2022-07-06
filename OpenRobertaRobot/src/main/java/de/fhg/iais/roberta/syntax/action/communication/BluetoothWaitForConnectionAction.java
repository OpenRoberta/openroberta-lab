package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robCommunication_waitForConnection"}, name = "BLUETOOTH_WAIT_FOR_CONNECTION_ACTION")
public final class BluetoothWaitForConnectionAction extends Action {

    public BluetoothWaitForConnectionAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

}
