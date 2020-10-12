package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

public final class CalliopeSimValidatorVisitor extends AbstractSimValidatorVisitor implements IMbedVisitor<Void> {

    public CalliopeSimValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        displayTextAction.getMsg().accept(this);
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        displayImageAction.getValuesToDisplay().accept(this);
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().accept(this);
        imageShiftFunction.getPositions().accept(this);
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        imageInvertFunction.getImage().accept(this);
        return null;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        checkSensorPort(temperatureSensor);
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        ultrasonicSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        infraredSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(ledOnAction.getPort());
        if ( usedActor == null ) {
            ledOnAction.addInfo(NepoInfo.warning("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        ledOnAction.getLedColor().accept(this);
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(lightStatusAction.getPort());
        if ( usedActor == null ) {
            lightStatusAction.addInfo(NepoInfo.warning("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        lightAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        radioSendAction.getMsg().accept(this);
        radioSendAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        radioReceiveAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        rgbColor.getR().accept(this);
        rgbColor.getG().accept(this);
        rgbColor.getB().accept(this);
        rgbColor.getA().accept(this);
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        checkSensorPort(pinValueSensor);
        if ( pinValueSensor.getMode().equals(SC.PULSEHIGH) || pinValueSensor.getMode().equals(SC.PULSELOW) || pinValueSensor.getMode().equals(SC.PULSE) ) {
            pinValueSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        colorSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(pinWriteValueAction.getPort());
        if ( usedActor == null ) {
            pinWriteValueAction.addInfo(NepoInfo.warning("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        pinWriteValueAction.getValue().accept(this);
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponentByType("BUZZER");
        if ( usedActor == null ) {
            toneAction.addInfo(NepoInfo.warning("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return super.visitToneAction(toneAction);
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponentByType("BUZZER");
        if ( usedActor == null ) {
            playNoteAction.addInfo(NepoInfo.warning("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        displaySetBrightnessAction.getBrightness().accept(this);
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        displaySetPixelAction.getBrightness().accept(this);
        displaySetPixelAction.getX().accept(this);
        displaySetPixelAction.getY().accept(this);
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        displayGetPixelAction.getX().accept(this);
        displayGetPixelAction.getY().accept(this);
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        radioSetChannelAction.getChannel().accept(this);
        radioSetChannelAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        radioRssiSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        accelerometerSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        checkSensorPort(keysSensor);
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        gyroSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        checkSensorPort(lightSensor);
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(motorOnAction.getUserDefinedPort());
        if ( usedActor == null ) {
            motorOnAction.addInfo(NepoInfo.warning("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        motorOnAction.getParam().getSpeed().accept(this);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(motorStopAction.getUserDefinedPort());
        if ( usedActor == null ) {
            motorStopAction.addInfo(NepoInfo.warning("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Void> fourDigitDisplayShowAction) {
        fourDigitDisplayShowAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Void> fourDigitDisplayClearAction) {
        fourDigitDisplayClearAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction<Void> ledBarSetAction) {
        ledBarSetAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPull) {
        pinSetPull.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(bothMotorsOnAction.getPortA());
        if ( usedActor == null ) {
            bothMotorsOnAction.addInfo(NepoInfo.warning("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        bothMotorsOnAction.getSpeedA().accept(this);
        bothMotorsOnAction.getSpeedB().accept(this);
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        humiditySensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction<Void> switchLedMatrixAction) {
        switchLedMatrixAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction<Void> servoSetAction) {
        servoSetAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction<Void> motionKitSingleSetAction) {
        motionKitSingleSetAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction<Void> motionKitDualSetAction) {
        motionKitDualSetAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }
}
