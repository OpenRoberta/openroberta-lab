package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousDriveRobots;
import de.fhg.iais.roberta.visitor.hardware.actor.IBluetoothVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface INxtVisitor<V> extends ISensorVisitor<V>, IActors4AutonomousDriveRobots<V>, IBluetoothVisitor<V> {

    @Override
    default V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitBluetoothConnectAction(BluetoothConnectAction<V> bluetoothConnectAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<V> bluetoothWaitForConnection) {
        throw new DbcException("Not supported!");
    }

}
