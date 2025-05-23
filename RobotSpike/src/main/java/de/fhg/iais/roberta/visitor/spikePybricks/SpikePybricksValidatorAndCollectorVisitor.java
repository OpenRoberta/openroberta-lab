package de.fhg.iais.roberta.visitor.spikePybricks;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedImport;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.AbstractSpikeValidatorAndCollectorVisitor;

public class SpikePybricksValidatorAndCollectorVisitor extends AbstractSpikeValidatorAndCollectorVisitor {

    public SpikePybricksValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }


    @Override
    final public Void visitMotorDiffTurnForAction(MotorDiffTurnForAction motorDiffTurnForAction) {
        super.visitMotorDiffTurnForAction(motorDiffTurnForAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_FROM_PERCENT);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.TURN_FOR);
        return null;
    }

    @Override
    final public Void visitKeysSensor(KeysSensor keysSensor) {
        super.visitKeysSensor(keysSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(keysSensor.getUserDefinedPort(), SC.BUTTON, SC.DEFAULT));
        return null;
    }

    @Override
    final public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        super.visitDisplayImageAction(displayImageAction);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.WAIT));
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SHOW_ANIMATION);
        return null;
    }

    @Override
    final public Void visitMotorDiffOnForAction(MotorDiffOnForAction motorDiffOnForAction) {
        super.visitMotorDiffOnForAction(motorDiffOnForAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_TO_MM_SEC);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.CIRCLE_CIRCUMFERENCE);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_FROM_PERCENT);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DRIVE_STRAIGHT);
        return null;
    }

    @Override
    final public Void visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction) {
        super.visitMotorDiffTurnAction(motorDiffTurnAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_FROM_PERCENT);
        if ( !motorDiffTurnAction.regulation )
            usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DIFFDRIVE);
        return null;
    }

    @Override
    final public Void visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction) {
        super.visitMotorDiffOnAction(motorDiffOnAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_FROM_PERCENT);
        if ( !motorDiffOnAction.regulation )
            usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DIFFDRIVE);
        else {
            usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_TO_MM_SEC);
            usedMethodBuilder.addUsedMethod(SpikePybricksMethods.CIRCLE_CIRCUMFERENCE);
        }
        return null;
    }

    @Override
    final public Void visitMotorDiffCurveForAction(MotorDiffCurveForAction motorDiffCurveForAction) {
        super.visitMotorDiffCurveForAction(motorDiffCurveForAction);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.WAIT));
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DIFFDRIVE);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.TANKDRIVE_DIST);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_TO_MM_SEC);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.CIRCLE_CIRCUMFERENCE);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_FROM_PERCENT);
        return null;
    }

    @Override
    final public Void visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction) {
        super.visitMotorDiffCurveAction(motorDiffCurveAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DIFFDRIVE);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_FROM_PERCENT);
        return null;
    }

    @Override
    final public void checkDiffDrive(Phrase phrase) {
        super.checkDiffDrive(phrase);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        ConfigurationComponent diffDrive = this.robotConfiguration.optConfigurationComponentByType("DIFFERENTIALDRIVE");
        if ( diffDrive != null ) {
            String leftUserPort = diffDrive.getComponentProperties().get("MOTOR_L");
            String rightUserPort = diffDrive.getComponentProperties().get("MOTOR_R");
            usedHardwareBuilder.addLockedComponent("MOTOR_L", leftUserPort);
            usedHardwareBuilder.addLockedComponent("MOTOR_R", rightUserPort);
        }
    }

    @Override
    final public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        super.visitRgbLedOnHiddenAction(rgbLedOnHiddenAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.HUB_LIGHT);
        return null;
    }

    @Override
    final public Void visitColorConst(ColorConst colorConst) {
        super.visitColorConst(colorConst);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.COlOR_IMPORT));
        return null;
    }

    @Override
    final public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        super.visitMotorOnForAction(motorOnForAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_FROM_PERCENT);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        return null;
    }

    @Override
    final public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        super.visitMotorOnAction(motorOnAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_FROM_PERCENT);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        return null;
    }

    final public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        super.visitMotorStopAction(motorStopAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DEG_SEC_FROM_PERCENT);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        return null;
    }

    @Override
    final public Void visitGestureSensor(GestureSensor gestureSensor) {
        super.visitGestureSensor(gestureSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor("GYRO", SC.GYRO, SC.DEFAULT));

        switch ( gestureSensor.getMode() ) {
            case "FREEFALL":
                usedMethodBuilder.addUsedMethod(SpikePybricksMethods.GET_ACCELERATION);
                usedMethodBuilder.addUsedMethod(SpikePybricksMethods.IS_FREE_FALL);
                usedHardwareBuilder.addUsedImport(new UsedImport(SC.WAIT));
                usedHardwareBuilder.addUsedSensor(new UsedSensor("TIMER_1", SC.TIMER, SC.DEFAULT));
                break;
            case "SHAKE":
                usedMethodBuilder.addUsedMethod(SpikePybricksMethods.IS_SHAKEN);
            case "TAPPED":
                usedMethodBuilder.addUsedMethod(SpikePybricksMethods.IS_TAPPED);
                usedHardwareBuilder.addUsedImport(new UsedImport(SC.WAIT));
                usedHardwareBuilder.addUsedSensor(new UsedSensor("TIMER_1", SC.TIMER, SC.DEFAULT));
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    final public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        super.visitPlayNoteAction(playNoteAction);
        usedHardwareBuilder.addUsedActor(new UsedActor("HUB", SC.SPEAKER));
        return null;
    }

    @Override
    public Void visitPlayToneAction(PlayToneAction playToneAction) {
        super.visitPlayToneAction(playToneAction);
        usedHardwareBuilder.addUsedActor(new UsedActor("HUB", SC.SPEAKER));
        return null;
    }

    @Override
    final public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.WAIT));
        return null;
    }

    @Override
    final public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        super.visitUltrasonicSensor(ultrasonicSensor);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.GET_DIST);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        return null;
    }

    @Override
    final public Void visitTouchSensor(TouchSensor touchSensor) {
        super.visitTouchSensor(touchSensor);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        return null;
    }

    @Override
    final public Void visitColorSensor(ColorSensor colorSensor) {
        super.visitColorSensor(colorSensor);
        switch ( colorSensor.getMode() ) {
            case "GREENCHANNEL":
            case "REDCHANNEL":
            case "BLUECHANNEL":
                usedMethodBuilder.addUsedMethod(SpikePybricksMethods.HSVTORGB);
                break;
            case "COLOUR":
                usedMethodBuilder.addUsedMethod(SpikePybricksMethods.GET_COLOR);
                break;
            default:
                break;
        }
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.COlOR_IMPORT));
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        return null;
    }

    @Override
    final public Void visitGyroSensor(GyroSensor gyroSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor("GYRO", SC.GYRO, SC.DEFAULT));
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        super.visitDisplayTextAction(displayTextAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DISPLAY_TEXT);
        return null;
    }

}
