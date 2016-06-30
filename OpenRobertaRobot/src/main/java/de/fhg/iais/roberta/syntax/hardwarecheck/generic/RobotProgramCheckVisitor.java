package de.fhg.iais.roberta.syntax.hardwarecheck.generic;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
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
            switch ( sensor.getKind() ) {
                case COLOR_SENSING:
                    if ( usedSensor.getName() != SensorType.COLOR ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case TOUCH_SENSING:
                    if ( usedSensor.getName() != SensorType.TOUCH ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case ULTRASONIC_SENSING:
                    if ( usedSensor.getName() != SensorType.ULTRASONIC ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case INFRARED_SENSING:
                    if ( usedSensor.getName() != SensorType.INFRARED ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case GYRO_SENSING:
                    if ( usedSensor.getName() != SensorType.GYRO ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
