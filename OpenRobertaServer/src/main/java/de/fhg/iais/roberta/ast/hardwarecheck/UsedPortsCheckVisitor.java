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
import de.fhg.iais.roberta.ev3.components.EV3Sensor;

public class UsedPortsCheckVisitor extends CheckVisitor {
    EV3BrickConfiguration brickConfiguration;

    public static ArrayList<ArrayList<Phrase<Void>>> check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, EV3BrickConfiguration brickConfiguration) {
        Assert.isTrue(phrasesSet.size() >= 1);
        UsedPortsCheckVisitor checkVisitor = new UsedPortsCheckVisitor(brickConfiguration);
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(checkVisitor);
            }
        }
        return phrasesSet;
    }

    private UsedPortsCheckVisitor(EV3BrickConfiguration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkLeftRightMotorPort(driveAction);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkLeftRightMotorPort(turnAction);
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
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        checkMotorPort(motorSetPowerAction);
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
            encoderSensor.addInfo(NepoInfo.warning(""));
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
            action.addInfo(NepoInfo.warning(""));
        }
    }

    private void checkLeftRightMotorPort(Phrase<Void> driveAction) {
        if ( this.brickConfiguration.getLeftMotorPort() == null ) {
            driveAction.addInfo(NepoInfo.warning(""));
        }
        if ( this.brickConfiguration.getRightMotorPort() == null ) {
            driveAction.addInfo(NepoInfo.warning(""));
        }
    }

    private void checkSensorPort(BaseSensor<Void> sensor) {
        EV3Sensor usedSensor = this.brickConfiguration.getSensorOnPort(sensor.getPort());
        if ( usedSensor == null ) {
            sensor.addInfo(NepoInfo.warning(""));
        } else {

        }
    }
}
