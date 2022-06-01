package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.util.syntax.SC;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.EdisonMethods;
import de.fhg.iais.roberta.visitor.IEdisonVisitor;

public class EdisonValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IEdisonVisitor<Void> {

    public EdisonValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        requiredComponentVisited(driveAction, driveAction.getParam().getSpeed());
        usedMethodBuilder.addUsedMethod(EdisonMethods.DIFFDRIVE);
        addUsedMethodsForDriveActions();
        driveAction.getParam().getSpeed().accept(this);
        if ( driveAction.getParam().getDuration() != null ) {
            requiredComponentVisited(driveAction, driveAction.getParam().getDuration().getValue());
            driveAction.getParam().getDuration().getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        // the block has the description "steer"
        requiredComponentVisited(curveAction, curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed());
        addUsedMethodsForDriveActions();
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        if ( curveAction.getParamLeft().getDuration() != null ) {
            requiredComponentVisited(curveAction, curveAction.getParamLeft().getDuration().getValue(), curveAction.getParamRight().getDuration().getValue());
            curveAction.getParamLeft().getDuration().getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        requiredComponentVisited(turnAction, turnAction.getParam().getSpeed());
        usedMethodBuilder.addUsedMethod(EdisonMethods.DIFFTURN);
        addUsedMethodsForDriveActions();
        turnAction.getParam().getSpeed().accept(this);
        if ( turnAction.getParam().getDuration() != null ) {
            requiredComponentVisited(turnAction, turnAction.getParam().getDuration().getValue());
            turnAction.getParam().getDuration().getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.getParam().getSpeed());
        usedMethodBuilder.addUsedMethod(EdisonMethods.MOTORON);
        usedMethodBuilder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        usedMethodBuilder.addUsedMethod(EdisonMethods.GETDIR);
        motorOnAction.getParam().getSpeed().accept(this);
        if ( motorOnAction.getParam().getDuration() != null ) {
            requiredComponentVisited(motorOnAction, motorOnAction.getParam().getDuration().getValue());
        }
        if ( motorOnAction.getDurationValue() != null ) {
            motorOnAction.getDurationValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        requiredComponentVisited(toneAction, toneAction.getFrequency(), toneAction.getDuration());
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        usedMethodBuilder.addUsedMethod(EdisonMethods.OBSTACLEDETECTION);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(infraredSensor.getUserDefinedPort(), SC.INFRARED, infraredSensor.getMode()));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        usedMethodBuilder.addUsedMethod(EdisonMethods.IRSEEK);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(irSeekerSensor.getUserDefinedPort(), SC.IRSEEKER, irSeekerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        requiredComponentVisited(sendIRAction, sendIRAction.getCode()); //?
        usedMethodBuilder.addUsedMethod(EdisonMethods.IRSEND);
        sendIRAction.getCode().accept(this);
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        usedMethodBuilder.addUsedMethod(EdisonMethods.IRSEEK);
        return null;
    }

    @Override
    public Void visitResetSensor(ResetSensor<Void> voidResetSensor) {
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

    /**
     * visit a {@link GetSampleSensor}.
     *
     * @param sensorGetSample to be visited
     */
    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        switch ( sensorGetSample.getSensorTypeAndMode() ) {
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
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    private void addUsedMethodsForDriveActions() {
        usedMethodBuilder.addUsedMethod(EdisonMethods.DIFFCURVE);
        usedMethodBuilder.addUsedMethod(EdisonMethods.SHORTEN); //used inside helper method
        usedMethodBuilder.addUsedMethod(EdisonMethods.GETDIR);
    }
}
