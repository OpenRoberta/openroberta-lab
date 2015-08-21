package de.fhg.iais.roberta.syntax.hardwarecheck.ev3;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.action.ev3.DriveAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.ev3.TurnAction;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.hardwarecheck.CheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.Assert;

public class UsedPortsCheckVisitor extends CheckVisitor {
    private ArrayList<ArrayList<Phrase<Void>>> checkedProgram;
    private int errorCount = 0;
    Ev3Configuration brickConfiguration;

    public UsedPortsCheckVisitor(Ev3Configuration brickConfiguration) {
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
        checkDiffDrive(driveAction);
        driveAction.getParam().getSpeed().visit(this);
        MotorDuration<Void> duration = driveAction.getParam().getDuration();
        if ( duration != null ) {
            duration.getValue().visit(this);
        }
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkDiffDrive(turnAction);
        turnAction.getParam().getSpeed().visit(this);
        MotorDuration<Void> duration = turnAction.getParam().getDuration();
        if ( duration != null ) {
            duration.getValue().visit(this);
        }
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
        Expr<Void> duration = motorOnAction.getDurationValue();
        if ( duration != null ) {
            duration.visit(this);
        }
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
        checkDiffDrive(stopAction);
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

    private void checkDiffDrive(Phrase<Void> driveAction) {
        checkLeftRightMotorPort(driveAction);

    }

    private void checkMotorPort(MoveAction<Void> action) {
        if ( this.brickConfiguration.getActorOnPort(action.getPort()) == null ) {
            action.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_MISSING"));
            this.errorCount++;
        }
    }

    private void checkLeftRightMotorPort(Phrase<Void> driveAction) {
        ActorPort leftMotorPort = this.brickConfiguration.getLeftMotorPort();
        ActorPort rightMotorPort = this.brickConfiguration.getRightMotorPort();
        if ( leftMotorPort == null ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_LEFT_MISSING"));
            this.errorCount++;
        } else if ( !this.brickConfiguration.getActorOnPort(leftMotorPort).isRegulated() ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_LEFT_UNREGULATED"));
            this.errorCount++;
        }
        if ( rightMotorPort == null ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING"));
            this.errorCount++;
        } else if ( !this.brickConfiguration.getActorOnPort(rightMotorPort).isRegulated() ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_RIGHT_UNREGULATED"));
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
