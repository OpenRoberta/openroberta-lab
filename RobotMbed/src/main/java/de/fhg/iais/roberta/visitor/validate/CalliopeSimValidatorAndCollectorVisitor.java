package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.actor.light.LightAction;
import de.fhg.iais.roberta.syntax.actor.mbed.*;
import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IMbedVisitorWithoutDefault;

public final class CalliopeSimValidatorAndCollectorVisitor extends MbedValidatorAndCollectorVisitor implements IMbedVisitorWithoutDefault<Void> {

    public CalliopeSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        addErrorToPhrase(ultrasonicSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitUltrasonicSensor(ultrasonicSensor);
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        addWarningToPhrase(lightAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitLightAction(lightAction);
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        addWarningToPhrase(radioSendAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioSendAction(radioSendAction);
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        addErrorToPhrase(radioReceiveAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioReceiveAction(radioReceiveAction);
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        if ( pinValueSensor.getMode().equals(SC.PULSEHIGH) || pinValueSensor.getMode().equals(SC.PULSELOW) || pinValueSensor.getMode().equals(SC.PULSE) ) {
            addErrorToPhrase(pinValueSensor, "SIM_BLOCK_NOT_SUPPORTED");
        }
        return super.visitPinGetValueSensor(pinValueSensor);
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        addErrorToPhrase(colorSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitColorSensor(colorSensor);
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        addWarningToPhrase(radioSetChannelAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioSetChannelAction(radioSetChannelAction);
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        addErrorToPhrase(radioRssiSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioRssiSensor(radioRssiSensor);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        addErrorToPhrase(accelerometerSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitAccelerometerSensor(accelerometerSensor);
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        addErrorToPhrase(gyroSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitGyroSensor(gyroSensor);
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        addWarningToPhrase(fourDigitDisplayShowAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitFourDigitDisplayShowAction(fourDigitDisplayShowAction);
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        addWarningToPhrase(fourDigitDisplayClearAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitFourDigitDisplayClearAction(fourDigitDisplayClearAction);
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        addWarningToPhrase(ledBarSetAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitLedBarSetAction(ledBarSetAction);
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        addErrorToPhrase(humiditySensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitHumiditySensor(humiditySensor);
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        addWarningToPhrase(switchLedMatrixAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitSwitchLedMatrixAction(switchLedMatrixAction);
    }

    @Override
    public Void visitServoSetAction(ServoSetAction servoSetAction) {
        addWarningToPhrase(servoSetAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitServoSetAction(servoSetAction);
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        addWarningToPhrase(motionKitSingleSetAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitMotionKitSingleSetAction(motionKitSingleSetAction);
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        addWarningToPhrase(motionKitDualSetAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitMotionKitDualSetAction(motionKitDualSetAction);
    }
}
