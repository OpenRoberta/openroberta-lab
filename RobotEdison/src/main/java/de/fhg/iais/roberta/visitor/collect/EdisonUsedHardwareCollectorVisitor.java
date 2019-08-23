package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.visitor.hardware.IEdisonVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * This class visits all the sensors/actors of the Edison brick and collects information about them
 */
public class EdisonUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IEdisonVisitor<Void> {
    private final Set<Method> usedMethods = EnumSet.noneOf(Method.class); //All needed helper methods as a Set

    public EdisonUsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, Configuration robotConfiguration) {
        super(robotConfiguration);
        check(programPhrases);
    }

    // TODO probably move somewhere else
    /**
     * Blockly Blocks that need an extra helper method in the source code
     */
    public enum Method {
        OBSTACLEDETECTION, //Obstacle detection
        IRSEND, //IR sender
        IRSEEK, //IR seeker
        MOTORON, //Motor on / motor on for... block
        SHORTEN, //shorten a number for Edisons drive() methods
        CURVE, //for the steer block
        DIFFDRIVE, //for driving
        DIFFTURN //for turning
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.usedMethods.add(Method.MOTORON);
        return super.visitMotorOnAction(motorOnAction);
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        this.usedMethods.add(Method.IRSEEK);
        return super.visitIRSeekerSensor(irSeekerSensor);
    }

    // TODO is this really needed and or used?
    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        this.usedMethods.add(Method.IRSEND);
        return super.visitBluetoothSendAction(bluetoothSendAction);
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.usedMethods.add(Method.OBSTACLEDETECTION);
        return super.visitInfraredSensor(infraredSensor);
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.usedMethods.add(Method.IRSEND);
        sendIRAction.getCode().visit(this);
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.usedMethods.add(Method.IRSEEK);
        return null;
    }

    @Override
    public Void visitSensorResetAction(ResetSensor<Void> vResetSensor) {
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        this.usedMethods.add(Method.DIFFDRIVE);
        return null;
    }
    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        if (curveAction.getParamLeft().getDuration() != null) {
            this.usedMethods.add(Method.CURVE);
        }
        return null;
    }

    //TODO-MAX helper methods for wait-until block
    //TODO-MAX JavaDoc
    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        return super.visitWaitStmt(waitStmt);
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        this.usedMethods.add(Method.DIFFTURN);
        return null;
    }
    /**
     * Returns all methods that need additional helper methods.
     *
     * @return all used methods
     */
    public Set<Method> getUsedMethods() {
        return Collections.unmodifiableSet(this.usedMethods);
    }

    /**
     * visit a {@link GetSampleSensor}.
     *
     * @param sensorGetSample to be visited
     */
    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        switch (sensorGetSample.getSensorTypeAndMode()) {
            case "INFRARED_OBSTACLE":
                this.usedMethods.add(Method.OBSTACLEDETECTION);
                break;
            case "IRSEEKER_RCCODE":
                this.usedMethods.add(Method.IRSEEK);
                break;
            default:
                break;
        }

        return null;
    }
}