package de.fhg.iais.roberta.syntax.hardwarecheck.generic;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class SimulationProgramCheckVisitor extends ProgramCheckVisitor {

    public SimulationProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        bluetoothReceiveAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        bluetoothConnectAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        bluetoothSendAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        bluetoothWaitForConnection.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        methodReturn.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        methodIfReturn.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        stmtFlowCon.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    protected void checkSensorPort(BaseSensor<Void> sensor) {
        Sensor usedSensor = this.brickConfiguration.getSensorOnPort(sensor.getPort());
        if ( usedSensor == null ) {
            if ( sensor.getKind() == BlockType.INFRARED_SENSING ) {
                sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_INFRARED_SENSOR_PORT"));
            } else {
                sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_SENSOR_MISSING"));
            }
        } else {
            switch ( sensor.getKind() ) {
                case COLOR_SENSING:
                    if ( usedSensor.getName() != SensorType.COLOR ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case TOUCH_SENSING:
                    if ( usedSensor.getName() != SensorType.TOUCH ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case ULTRASONIC_SENSING:
                    if ( usedSensor.getName() != SensorType.ULTRASONIC ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case INFRARED_SENSING:
                    if ( usedSensor.getName() != SensorType.INFRARED ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_INFRARED_SENSOR_PORT"));
                    }
                    break;
                case GYRO_SENSING:
                    if ( usedSensor.getName() != SensorType.GYRO ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
