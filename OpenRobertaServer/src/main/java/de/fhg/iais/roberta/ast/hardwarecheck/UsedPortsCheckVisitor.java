package de.fhg.iais.roberta.ast.hardwarecheck;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MoveAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.typecheck.NepoInfo;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.ev3.EV3Sensors;
import de.fhg.iais.roberta.ev3.components.EV3Sensor;

public class UsedPortsCheckVisitor extends CheckVisitor {
    private ArrayList<ArrayList<Phrase<Void>>> checkedProgram;
    private int errorCount = 0;
    EV3BrickConfiguration brickConfiguration;

    public UsedPortsCheckVisitor(EV3BrickConfiguration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
    }

    public int check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(phrasesSet.size() >= 1);
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(this);
            }
        }
        this.checkedProgram = phrasesSet;
        return this.errorCount;
    }

    /**
     * @return the checkedProgram
     */
    public ArrayList<ArrayList<Phrase<Void>>> getCheckedProgram() {
        return this.checkedProgram;
    }

    /**
     * @return the countErrors
     */
    public int getErrorCount() {
        return this.errorCount;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkLeftRightMotorPort(driveAction);
        driveAction.getParam().getSpeed().visit(this);
        driveAction.getParam().getDuration().getValue().visit(this);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkLeftRightMotorPort(turnAction);
        turnAction.getParam().getSpeed().visit(this);
        turnAction.getParam().getDuration().getValue().visit(this);
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        checkMotorPort(motorGetPowerAction);
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        checkMotorPort(motorOnAction);
        motorOnAction.getDurationValue().visit(this);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        checkMotorPort(motorSetPowerAction);
        motorSetPowerAction.getPower().visit(this);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        checkMotorPort(motorStopAction);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        checkLeftRightMotorPort(stopAction);
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        checkSensorPort(colorSensor);
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        if ( this.brickConfiguration.getActorOnPort(encoderSensor.getMotor()) == null ) {
            encoderSensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_MISSING"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        checkSensorPort(gyroSensor);
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        checkSensorPort(infraredSensor);
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        checkSensorPort(touchSensor);
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        return null;
    }

    private void checkMotorPort(MoveAction<Void> action) {
        if ( this.brickConfiguration.getActorOnPort(action.getPort()) == null ) {
            action.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_MISSING"));
            this.errorCount++;
        }
    }

    private void checkLeftRightMotorPort(Phrase<Void> driveAction) {
        if ( this.brickConfiguration.getLeftMotorPort() == null ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_LEFT_MISSING"));
            this.errorCount++;
        }
        if ( this.brickConfiguration.getRightMotorPort() == null ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING"));
            this.errorCount++;
        }
    }

    private void checkSensorPort(BaseSensor<Void> sensor) {
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
