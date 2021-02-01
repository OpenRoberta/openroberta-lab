package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.visitor.IMbedVisitorWithoutDefault;

public final class MicrobitSimValidatorAndCollectorVisitor extends MbedValidatorAndCollectorVisitor implements IMbedVisitorWithoutDefault<Void> {

    public MicrobitSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        addWarningToPhrase(motorOnAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitMotorOnAction(motorOnAction);
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        addWarningToPhrase(radioSendAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioSendAction(radioSendAction);
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        addWarningToPhrase(radioReceiveAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioReceiveAction(radioReceiveAction);
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        if ( pinValueSensor.getMode().equals(SC.PULSEHIGH) || pinValueSensor.getMode().equals(SC.PULSELOW) || pinValueSensor.getMode().equals(SC.PULSE) ) {
            addWarningToPhrase(pinValueSensor, "SIM_BLOCK_NOT_SUPPORTED");
        }
        return super.visitPinGetValueSensor(pinValueSensor);
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        addWarningToPhrase(radioSetChannelAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioSetChannelAction(radioSetChannelAction);
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        addWarningToPhrase(radioRssiSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioRssiSensor(radioRssiSensor);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor) {
        addWarningToPhrase(accelerometerSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitAccelerometerSensor(accelerometerSensor);
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Void> fourDigitDisplayShowAction) {
        addWarningToPhrase(fourDigitDisplayShowAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitFourDigitDisplayShowAction(fourDigitDisplayShowAction);
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Void> fourDigitDisplayClearAction) {
        addWarningToPhrase(fourDigitDisplayClearAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitFourDigitDisplayClearAction(fourDigitDisplayClearAction);
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction<Void> ledBarSetAction) {
        addWarningToPhrase(ledBarSetAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitLedBarSetAction(ledBarSetAction);
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction<Void> switchLedMatrixAction) {
        addWarningToPhrase(switchLedMatrixAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitSwitchLedMatrixAction(switchLedMatrixAction);
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPullAction) {
        addWarningToPhrase(pinSetPullAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitPinSetPullAction(pinSetPullAction);
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        addWarningToPhrase(bothMotorsOnAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        addWarningToPhrase(bothMotorsStopAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitBothMotorsStopAction(bothMotorsStopAction);
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        addWarningToPhrase(humiditySensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitHumiditySensor(humiditySensor);
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        addWarningToPhrase(ultrasonicSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitUltrasonicSensor(ultrasonicSensor);
    }

}
