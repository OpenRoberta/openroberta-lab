package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface IBluetoothVisitor<V> extends IHardwareVisitor<V> {

    V visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction);

    V visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction);

    V visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction);

    V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection);

    V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction);

}