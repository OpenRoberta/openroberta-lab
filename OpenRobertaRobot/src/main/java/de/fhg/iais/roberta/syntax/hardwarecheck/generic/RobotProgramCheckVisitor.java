package de.fhg.iais.roberta.syntax.hardwarecheck.generic;

import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class RobotProgramCheckVisitor extends ProgramCheckVisitor {

    public RobotProgramCheckVisitor(Ev3Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    protected void checkSensorPort(BaseSensor<Void> sensor) {
        EV3Sensor usedSensor = this.brickConfiguration.getSensorOnPort(sensor.getPort());
        if ( usedSensor == null ) {
            sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        } else {
            switch ( sensor.getKind() ) {
                case COLOR_SENSING:
                    if ( usedSensor.getComponentTypeName() != EV3Sensors.EV3_COLOR_SENSOR.getTypeName() ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case TOUCH_SENSING:
                    if ( usedSensor.getComponentTypeName() != EV3Sensors.EV3_TOUCH_SENSOR.getTypeName() ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case ULTRASONIC_SENSING:
                    if ( usedSensor.getComponentTypeName() != EV3Sensors.EV3_ULTRASONIC_SENSOR.getTypeName() ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case INFRARED_SENSING:
                    if ( usedSensor.getComponentTypeName() != EV3Sensors.EV3_IR_SENSOR.getTypeName() ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case GYRO_SENSING:
                    if ( usedSensor.getComponentTypeName() != EV3Sensors.EV3_GYRO_SENSOR.getTypeName() ) {
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
