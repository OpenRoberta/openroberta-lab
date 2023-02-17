package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.EdisonMethods;
import de.fhg.iais.roberta.visitor.IEdisonVisitor;

public class EdisonValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IEdisonVisitor<Void> {

    public EdisonValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        requiredComponentVisited(driveAction, driveAction.param.getSpeed());
        usedMethodBuilder.addUsedMethod(EdisonMethods.DIFFDRIVE);
        addUsedMethodsForDriveActions();
        driveAction.param.getSpeed().accept(this);
        if ( driveAction.param.getDuration() != null ) {
            requiredComponentVisited(driveAction, driveAction.param.getDuration().getValue());
            driveAction.param.getDuration().getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        // the block has the description "steer"
        requiredComponentVisited(curveAction, curveAction.paramLeft.getSpeed(), curveAction.paramRight.getSpeed());
        addUsedMethodsForDriveActions();
        curveAction.paramLeft.getSpeed().accept(this);
        curveAction.paramRight.getSpeed().accept(this);
        if ( curveAction.paramLeft.getDuration() != null ) {
            requiredComponentVisited(curveAction, curveAction.paramLeft.getDuration().getValue(), curveAction.paramRight.getDuration().getValue());
            curveAction.paramLeft.getDuration().getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        requiredComponentVisited(turnAction, turnAction.param.getSpeed());
        usedMethodBuilder.addUsedMethod(EdisonMethods.DIFFTURN);
        addUsedMethodsForDriveActions();
        turnAction.param.getSpeed().accept(this);
        if ( turnAction.param.getDuration() != null ) {
            requiredComponentVisited(turnAction, turnAction.param.getDuration().getValue());
            turnAction.param.getDuration().getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.param.getSpeed());
        usedMethodBuilder.addUsedMethod(EdisonMethods.MOTORON);
        usedMethodBuilder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        usedMethodBuilder.addUsedMethod(EdisonMethods.GETDIR);
        motorOnAction.param.getSpeed().accept(this);
        if ( motorOnAction.param.getDuration() != null ) {
            requiredComponentVisited(motorOnAction, motorOnAction.param.getDuration().getValue());
        }
        if ( motorOnAction.getDurationValue() != null ) {
            motorOnAction.getDurationValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        requiredComponentVisited(toneAction, toneAction.frequency, toneAction.duration);
        if ( !toneAction.frequency.getClass().equals(NumConst.class) ) {
            addErrorToPhrase(toneAction, "NO_CONST_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        return null;
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        usedMethodBuilder.addUsedMethod(EdisonMethods.OBSTACLEDETECTION);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(infraredSensor.getUserDefinedPort(), SC.INFRARED, infraredSensor.getMode()));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        usedMethodBuilder.addUsedMethod(EdisonMethods.IRSEEK);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(irSeekerSensor.getUserDefinedPort(), SC.IRSEEKER, irSeekerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        requiredComponentVisited(sendIRAction, sendIRAction.code); //?
        usedMethodBuilder.addUsedMethod(EdisonMethods.IRSEND);
        sendIRAction.code.accept(this);
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        usedMethodBuilder.addUsedMethod(EdisonMethods.IRSEEK);
        return null;
    }

    @Override
    public Void visitResetSensor(ResetSensor voidResetSensor) {
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        //visit all statements to add their helper methods
        for ( Stmt s : waitStmt.statements.get() ) {
            s.accept(this);
        }
        return super.visitWaitStmt(waitStmt);
    }

    /**
     * visit a {@link GetSampleSensor}.
     *
     * @param sensorGetSample to be visited
     */
    @Override
    public Void visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        switch ( sensorGetSample.sensorTypeAndMode ) {
            case "INFRARED_OBSTACLE":
                usedMethodBuilder.addUsedMethod(EdisonMethods.OBSTACLEDETECTION);
                break;
            case "IRSEEKER_RCCODE":
                usedMethodBuilder.addUsedMethod(EdisonMethods.IRSEEK);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    private void addUsedMethodsForDriveActions() {
        usedMethodBuilder.addUsedMethod(EdisonMethods.DIFFCURVE);
        usedMethodBuilder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        usedMethodBuilder.addUsedMethod(EdisonMethods.GETDIR);
    }
}
