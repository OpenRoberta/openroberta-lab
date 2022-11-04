package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.actor.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.actor.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousDriveRobots;
import de.fhg.iais.roberta.visitor.hardware.actor.IBluetoothVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface INxtVisitor<V> extends ISensorVisitor<V>, IActors4AutonomousDriveRobots<V>, IBluetoothVisitor<V> {
    
    @Override
    default V visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection) {
        throw new DbcException("Not supported!");
    }

}
