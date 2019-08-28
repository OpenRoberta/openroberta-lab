package de.fhg.iais.roberta.visitor.collect;

import static de.fhg.iais.roberta.visitor.collect.EdisonMethods.CURVE;
import static de.fhg.iais.roberta.visitor.collect.EdisonMethods.DIFFDRIVE;
import static de.fhg.iais.roberta.visitor.collect.EdisonMethods.DIFFTURN;
import static de.fhg.iais.roberta.visitor.collect.EdisonMethods.IRSEEK;
import static de.fhg.iais.roberta.visitor.collect.EdisonMethods.IRSEND;
import static de.fhg.iais.roberta.visitor.collect.EdisonMethods.MOTORON;
import static de.fhg.iais.roberta.visitor.collect.EdisonMethods.OBSTACLEDETECTION;
import static de.fhg.iais.roberta.visitor.collect.EdisonMethods.READDIST;
import static de.fhg.iais.roberta.visitor.collect.EdisonMethods.SHORTEN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.visitor.hardware.IEdisonVisitor;

/**
 * This class visits all the sensors/actors of the Edison brick and collects information about them
 */
public class EdisonUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IEdisonVisitor<Void> {
    private final Set<EdisonMethods> usedMethods = EnumSet.noneOf(EdisonMethods.class); //All needed helper methods as a Set

    public EdisonUsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, Configuration robotConfiguration) {
        super(robotConfiguration);
        check(programPhrases);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.usedMethods.add(MOTORON);
        this.usedMethods.add(SHORTEN); //used inside helper method
        return super.visitMotorOnAction(motorOnAction);
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        this.usedMethods.add(IRSEEK);
        return super.visitIRSeekerSensor(irSeekerSensor);
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.usedMethods.add(OBSTACLEDETECTION);
        return super.visitInfraredSensor(infraredSensor);
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.usedMethods.add(IRSEND);
        sendIRAction.getCode().visit(this);
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.usedMethods.add(IRSEEK);
        return null;
    }

    @Override
    public Void visitSensorResetAction(ResetSensor<Void> vResetSensor) {
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        this.usedMethods.add(DIFFDRIVE);
        this.usedMethods.add(SHORTEN); //used inside helper method
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
        this.usedMethods.add(READDIST);
        this.usedMethods.add(CURVE);
        this.usedMethods.add(SHORTEN); //used inside helper method
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        //visit all statements to add their helper methods
        for ( Stmt<Void> s : waitStmt.getStatements().get() ) {
            s.visit(this);
        }
        return super.visitWaitStmt(waitStmt);
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        this.usedMethods.add(DIFFTURN);
        this.usedMethods.add(SHORTEN); //used inside helper method
        return null;
    }

    /**
     * Returns all methods that need additional helper methods.
     *
     * @return all used methods
     */
    public Set<EdisonMethods> getUsedMethods() {
        return Collections.unmodifiableSet(this.usedMethods);
    }

    /**
     * visit a {@link GetSampleSensor}.
     *
     * @param sensorGetSample to be visited
     */
    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        switch ( sensorGetSample.getSensorTypeAndMode() ) {
            case "INFRARED_OBSTACLE":
                this.usedMethods.add(OBSTACLEDETECTION);
                break;
            case "IRSEEKER_RCCODE":
                this.usedMethods.add(IRSEEK);
                break;
            default:
                break;
        }

        return null;
    }
}