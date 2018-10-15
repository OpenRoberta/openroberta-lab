package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ICommunicationVisitor<V> extends IHardwareVisitor<V> {

    /**
     * visit a {@link BluetoothRecieveAction}.
     *
     * @param bluetoothReceiveActionbluetoothReceiveAction to be visited
     */
    V visitBluetoothReceiveAction(BluetoothReceiveAction<V> bluetoothReceiveAction);

    /**
     * visit a {@link BluetoothConnectAction}.
     *
     * @param bluetoothConnectAction to be visited
     */
    V visitBluetoothConnectAction(BluetoothConnectAction<V> bluetoothConnectAction);

    /**
     * visit a {@link BluetoothSendAction}.
     *
     * @param bluetoothSendAction to be visited
     */
    V visitBluetoothSendAction(BluetoothSendAction<V> bluetoothSendAction);

    /**
     * visit a {@link BluetoothWaitForConnectionAction}.
     *
     * @param bluetoothWaitForConnection to be visited
     */
    V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<V> bluetoothWaitForConnection);

    /**
     * visit a {@link BluetoothCheckConnectAction}.
     *
     * @param bluetoothCheckConnectAction to be visited
     */
    V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<V> bluetoothCheckConnectAction);

}