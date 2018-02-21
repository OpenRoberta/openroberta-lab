package de.fhg.iais.roberta.syntax.check.program;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public abstract class RobotSimulationCheckVisitor extends RobotCommonCheckVisitor {

    public RobotSimulationCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        Sensor usedSensor = this.brickConfiguration.getSensorOnPort((ISensorPort) sensor.getPort());
        if ( usedSensor == null ) {
            if ( sensor.getKind().hasName("INFRARED_SENSING") ) {
                sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_INFRARED_SENSOR_PORT"));
            } else {
                sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_SENSOR_MISSING"));
            }
        } else {
            switch ( sensor.getKind().getName() ) {
                case "COLOR_SENSING":
                    if ( usedSensor.getType() != SensorType.COLOR && usedSensor.getType() != SensorType.HT_COLOR ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "TOUCH_SENSING":
                    if ( usedSensor.getType() != SensorType.TOUCH ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "ULTRASONIC_SENSING":
                    if ( usedSensor.getType() != SensorType.ULTRASONIC ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "INFRARED_SENSING":
                    if ( usedSensor.getType() != SensorType.INFRARED ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_INFRARED_SENSOR_PORT"));
                    }
                    break;
                case "GYRO_SENSING":
                    if ( usedSensor.getType() != SensorType.GYRO ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "COMPASS_SENSING":
                    if ( usedSensor.getType() != SensorType.COMPASS ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "IRSEEKER_SENSING":
                    if ( usedSensor.getType() != SensorType.IRSEEKER ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        stmtFlowCon.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        super.visitBluetoothReceiveAction(bluetoothReceiveAction);
        bluetoothReceiveAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        super.visitBluetoothConnectAction(bluetoothConnectAction);
        bluetoothConnectAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        super.visitBluetoothSendAction(bluetoothSendAction);
        bluetoothSendAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        super.visitBluetoothWaitForConnectionAction(bluetoothWaitForConnection);
        bluetoothWaitForConnection.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        super.visitConnectConst(connectConst);
        connectConst.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        super.visitBluetoothCheckConnectAction(bluetoothCheckConnectAction);
        bluetoothCheckConnectAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        super.visitTemperatureSensor(temperatureSensor);
        temperatureSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        super.visitLightSensor(lightSensor);
        if ( lightSensor.getMode().toString().equals("AMBIENTLIGHT") ) {
            lightSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        super.visitColorSensor(colorSensor);
        if ( colorSensor.getMode().toString().equals("AMBIENTLIGHT") ) {
            colorSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        }
        return null;
    }
}
