package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.bean.UsedHardwareBean.EdisonMethods;
import de.fhg.iais.roberta.components.ConfigurationAst;
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

    public EdisonUsedHardwareCollectorVisitor(Builder builder, ArrayList<ArrayList<Phrase<Void>>> programPhrases, ConfigurationAst robotConfiguration) {
        super(builder, robotConfiguration);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.builder.addUsedMethod(EdisonMethods.MOTORON);
        this.builder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        this.builder.addUsedMethod(EdisonMethods.GETDIR);
        return null;
        //        return super.visitMotorOnAction(motorOnAction);
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        this.builder.addUsedMethod(EdisonMethods.IRSEEK);
        return super.visitIRSeekerSensor(irSeekerSensor);
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.builder.addUsedMethod(EdisonMethods.OBSTACLEDETECTION);
        return super.visitInfraredSensor(infraredSensor);
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.builder.addUsedMethod(EdisonMethods.IRSEND);
        sendIRAction.getCode().accept(this);
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.builder.addUsedMethod(EdisonMethods.IRSEEK);
        return null;
    }

    @Override
    public Void visitSensorResetAction(ResetSensor<Void> vResetSensor) {
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        this.builder.addUsedMethod(EdisonMethods.DIFFDRIVE);
        this.builder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        this.builder.addUsedMethod(EdisonMethods.GETDIR);
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
        this.builder.addUsedMethod(EdisonMethods.DIFFCURVE);
        this.builder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        this.builder.addUsedMethod(EdisonMethods.GETDIR);
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        //visit all statements to add their helper methods
        for ( Stmt<Void> s : waitStmt.getStatements().get() ) {
            s.accept(this);
        }
        return super.visitWaitStmt(waitStmt);
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        this.builder.addUsedMethod(EdisonMethods.DIFFTURN);
        this.builder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        this.builder.addUsedMethod(EdisonMethods.GETDIR);
        return null;
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
                this.builder.addUsedMethod(EdisonMethods.OBSTACLEDETECTION);
                break;
            case "IRSEEKER_RCCODE":
                this.builder.addUsedMethod(EdisonMethods.IRSEEK);
                break;
            default:
                break;
        }

        return null;
    }
}