package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(category = "ACTOR", blocklyNames = {"robCommunication_waitForConnection"}, name = "BLUETOOTH_WAIT_FOR_CONNECTION_ACTION", blocklyType = BlocklyType.CONNECTION)
public final class BluetoothWaitForConnectionAction extends Action {

    public BluetoothWaitForConnectionAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
