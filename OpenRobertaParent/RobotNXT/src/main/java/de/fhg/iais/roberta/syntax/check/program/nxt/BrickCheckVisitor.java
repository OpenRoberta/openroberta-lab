package de.fhg.iais.roberta.syntax.check.program.nxt;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.visitor.actor.AstActorCommunicationVisitor;
import de.fhg.iais.roberta.visitor.nxt.NxtAstVisitor;

public class BrickCheckVisitor extends RobotBrickCheckVisitor implements NxtAstVisitor<Void>, AstActorCommunicationVisitor<Void> {

    public BrickCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        bluetoothReceiveAction.getConnection().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        bluetoothConnectAction.getAddress().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        bluetoothSendAction.getConnection().visit(this);
        bluetoothSendAction.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        bluetoothCheckConnectAction.getConnection().visit(this);
        return null;
    }

}
