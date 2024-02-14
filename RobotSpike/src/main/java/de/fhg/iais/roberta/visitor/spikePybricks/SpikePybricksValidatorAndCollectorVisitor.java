package de.fhg.iais.roberta.visitor.spikePybricks;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedImport;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
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
    final public Void visitKeysSensor(KeysSensor keysSensor) {
        super.visitKeysSensor(keysSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(keysSensor.getUserDefinedPort(), SC.BUTTON, SC.DEFAULT));
        return null;
    }

    @Override
    final public Void visitMotorDiffOnForAction(MotorDiffOnForAction motorDiffOnForAction){
        super.visitMotorDiffOnForAction(motorDiffOnForAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
        return null;
    }

    @Override
    final public Void visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction){
        super.visitMotorDiffTurnAction(motorDiffTurnAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.DIFFDRIVE);
        return null;
    }

    @Override
    final public Void visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction){
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
    final public Void visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction){
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
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.COlOR_CONST));
        return null;
    }

    @Override
    final public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        super.visitMotorOnForAction(motorOnForAction);
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.SPEED_FROM_PERCENT);
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
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.SQRT));
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.WAIT));
        //Port is a place-holder there is no Port that the Timer is connected to pybricks only has one Timer
        //Timer is listed as Sensor in OpenRobertaLab so i use addUsedSensor here
        usedHardwareBuilder.addUsedSensor(new UsedSensor("TIMER_1", SC.TIMER, SC.DEFAULT));
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.GESTURES);
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
        usedMethodBuilder.addUsedMethod(SpikePybricksMethods.HSVTORGB);
        usedHardwareBuilder.addUsedImport(new UsedImport(SC.PORT));
        return null;
    }

    @Override
    final public Void visitGyroSensor(GyroSensor gyroSensor) {
        super.visitGyroSensor(gyroSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor("GYRO", SC.GYRO, SC.DEFAULT));
        return null;
    }

}
