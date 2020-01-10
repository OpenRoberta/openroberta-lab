package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
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

    private final UsedMethodBean.Builder usedMethodBeanBuilder;

    public EdisonUsedHardwareCollectorVisitor(UsedHardwareBean.Builder usedHardwareBeanBuilder, UsedMethodBean.Builder usedMethodBeanBuilder, ConfigurationAst robotConfiguration) {
        super(usedHardwareBeanBuilder, robotConfiguration);
        this.usedMethodBeanBuilder = usedMethodBeanBuilder;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.MOTORON);
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.GETDIR);
        motorOnAction.getParam().getSpeed().accept(this);
        if ( motorOnAction.getParam().getDuration() != null ) {
            motorOnAction.getDurationValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.IRSEEK);
        return super.visitIRSeekerSensor(irSeekerSensor);
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.OBSTACLEDETECTION);
        return super.visitInfraredSensor(infraredSensor);
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.IRSEND);
        sendIRAction.getCode().accept(this);
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.IRSEEK);
        return null;
    }

    @Override
    public Void visitSensorResetAction(ResetSensor<Void> vResetSensor) {
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.DIFFDRIVE);
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.GETDIR);
        driveAction.getParam().getSpeed().accept(this);
        if ( driveAction.getParam().getDuration() != null ) {
            driveAction.getParam().getDuration().getValue().accept(this);
        }
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
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.DIFFCURVE);
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.GETDIR);
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        if ( curveAction.getParamLeft().getDuration() != null ) {
            curveAction.getParamLeft().getDuration().getValue().accept(this);
        }
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
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.DIFFTURN);
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.GETDIR);
        turnAction.getParam().getSpeed().accept(this);
        if ( turnAction.getParam().getDuration() != null ) {
            turnAction.getParam().getDuration().getValue().accept(this);
        }
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
                this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.OBSTACLEDETECTION);
                break;
            case "IRSEEKER_RCCODE":
                this.usedMethodBeanBuilder.addUsedMethod(EdisonMethods.IRSEEK);
                break;
            default:
                break;
        }

        return null;
    }
}