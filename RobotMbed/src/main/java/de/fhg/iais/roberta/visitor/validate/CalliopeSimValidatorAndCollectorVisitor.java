package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.visitor.IMbedVisitorWithoutDefault;

public final class CalliopeSimValidatorAndCollectorVisitor extends MbedValidatorAndCollectorVisitor implements IMbedVisitorWithoutDefault<Void> {

    public CalliopeSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        addWarningToPhrase(ultrasonicSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitUltrasonicSensor(ultrasonicSensor);
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        addWarningToPhrase(lightAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitLightAction(lightAction);
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
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        addWarningToPhrase(colorSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitColorSensor(colorSensor);
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
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        addWarningToPhrase(gyroSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitGyroSensor(gyroSensor);
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
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPull) {
        addWarningToPhrase(pinSetPull, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitPinSetPullAction(pinSetPull);
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        addWarningToPhrase(humiditySensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitHumiditySensor(humiditySensor);
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction<Void> switchLedMatrixAction) {
        addWarningToPhrase(switchLedMatrixAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitSwitchLedMatrixAction(switchLedMatrixAction);
    }

    @Override
    public Void visitServoSetAction(ServoSetAction<Void> servoSetAction) {
        addWarningToPhrase(servoSetAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitServoSetAction(servoSetAction);
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction<Void> motionKitSingleSetAction) {
        addWarningToPhrase(motionKitSingleSetAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitMotionKitSingleSetAction(motionKitSingleSetAction);
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction<Void> motionKitDualSetAction) {
        addWarningToPhrase(motionKitDualSetAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitMotionKitDualSetAction(motionKitDualSetAction);
    }
}
