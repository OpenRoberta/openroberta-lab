package de.fhg.iais.roberta.syntax.hardwarecheck.generic;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class RobotProgramCheckVisitor extends ProgramCheckVisitor {

    public RobotProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    protected void checkSensorPort(BaseSensor<Void> sensor) {
        Sensor usedSensor = this.brickConfiguration.getSensorOnPort(sensor.getPort());
        if ( usedSensor == null ) {
            sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        } else {
            switch ( sensor.getKind().getName() ) {
                case "COLOR_SENSING":
                    if ( usedSensor.getType() != SensorType.COLOR ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "TOUCH_SENSING":
                    if ( usedSensor.getType() != SensorType.TOUCH ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "ULTRASONIC_SENSING":
                    if ( usedSensor.getType() != SensorType.ULTRASONIC ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "INFRARED_SENSING":
                    if ( usedSensor.getType() != SensorType.INFRARED ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "GYRO_SENSING":
                    if ( usedSensor.getType() != SensorType.GYRO ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "SOUND_SENSING":
                    if ( usedSensor.getType() != SensorType.SOUND ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "LIGHT_SENSING":
                    if ( usedSensor.getType() != SensorType.LIGHT ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        // TODO Auto-generated method stub
        return null;
    }

}
