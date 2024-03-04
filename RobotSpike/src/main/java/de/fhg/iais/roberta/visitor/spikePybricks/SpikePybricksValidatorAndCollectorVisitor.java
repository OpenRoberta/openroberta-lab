package de.fhg.iais.roberta.visitor.spikePybricks;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedImport;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.spike.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorStopAction;
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
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
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
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DRIVE_STRAIGHT);
        return null;
    }

    @Override
    final public Void visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction) {
        super.visitMotorDiffTurnAction(motorDiffTurnAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DIFFDRIVE);
        return null;
    }

    @Override
    final public Void visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction) {
        super.visitMotorDiffOnAction(motorDiffOnAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DIFFDRIVE);
        return null;
    }

    @Override
    final public Void visitMotorDiffCurveForAction(MotorDiffCurveForAction motorDiffCurveForAction) {
        super.visitMotorDiffCurveForAction(motorDiffCurveForAction);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.WAIT));
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DIFFDRIVE);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.TANKDRIVE_DIST);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
        return null;
    }

    @Override
    final public Void visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction) {
        super.visitMotorDiffCurveAction(motorDiffCurveAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DIFFDRIVE);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
        return null;
    }

    @Override
    final public void checkDiffDrive(Phrase phrase) {
        super.checkDiffDrive(phrase);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
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
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        return null;
    }

    @Override
    final public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        super.visitMotorOnAction(motorOnAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        return null;
    }

    final public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        super.visitMotorStopAction(motorStopAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
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
    final public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.WAIT));
        return null;
    }

    @Override
    final public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        super.visitUltrasonicSensor(ultrasonicSensor);
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
            default:
                break;
        }
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.GET_COLOR);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.COlOR_IMPORT));
        return null;
    }

    @Override
    final public Void visitGyroSensor(GyroSensor gyroSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor("GYRO", SC.GYRO, SC.DEFAULT));
        return null;
    }

}
