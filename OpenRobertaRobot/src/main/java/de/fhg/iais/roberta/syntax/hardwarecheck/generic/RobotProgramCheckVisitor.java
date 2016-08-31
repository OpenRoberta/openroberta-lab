package de.fhg.iais.roberta.syntax.hardwarecheck.generic;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.syntax.action.generic.CurveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightSensorAction;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class RobotProgramCheckVisitor extends ProgramCheckVisitor {

    public RobotProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    protected void checkSensorPort(BaseSensor<Void> sensor) {
        Sensor usedSensor = brickConfiguration.getSensorOnPort(sensor.getPort());
        if ( usedSensor == null ) {
            sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            errorCount++;
        } else {
            switch ( sensor.getKind() ) {
                case COLOR_SENSING:
                    if ( usedSensor.getType() != SensorType.COLOR ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        errorCount++;
                    }
                    break;
                case TOUCH_SENSING:
                    if ( usedSensor.getType() != SensorType.TOUCH ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        errorCount++;
                    }
                    break;
                case ULTRASONIC_SENSING:
                    if ( usedSensor.getType() != SensorType.ULTRASONIC ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        errorCount++;
                    }
                    break;
                case INFRARED_SENSING:
                    if ( usedSensor.getType() != SensorType.INFRARED ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        errorCount++;
                    }
                    break;
                case GYRO_SENSING:
                    if ( usedSensor.getType() != SensorType.GYRO ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        errorCount++;
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> driveAction) {
        // TODO Auto-generated method stub
        return null;
    }
}
