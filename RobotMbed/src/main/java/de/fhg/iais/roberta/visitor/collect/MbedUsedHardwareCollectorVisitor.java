package de.fhg.iais.roberta.visitor.collect;

import com.google.common.collect.ClassToInstanceMap;

import java.util.List;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
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
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public final class MbedUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IMbedVisitor<Void> {

    public MbedUsedHardwareCollectorVisitor(
        List<List<Phrase<Void>>> phrasesSet,
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        displayTextAction.getMsg().accept(this);
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        displayImageAction.getValuesToDisplay().accept(this);
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        radioSendAction.getMsg().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        radioSetChannelAction.getChannel().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(gestureSensor.getPort(), SC.ACCELEROMETER, gestureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(compassSensor.getPort(), SC.ACCELEROMETER, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
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
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        this
            .getBuilder(UsedHardwareBean.Builder.class)
            .addUsedSensor(new UsedSensor(accelerometerSensor.getPort(), SC.ACCELEROMETER, accelerometerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().accept(this);
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        imageInvertFunction.getImage().accept(this);
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        ledOnAction.getLedColor().accept(this);
        String port = ledOnAction.getPort();
        ConfigurationComponent cc = this.robotConfiguration.optConfigurationComponent(port);
        if ( (cc != null) && cc.getComponentType().equals("CALLIBOT") ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.CALLIBOT));
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.CALLIBOT));
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        String port = lightStatusAction.getPort();
        ConfigurationComponent cc = this.robotConfiguration.optConfigurationComponent(port);
        if ( (cc != null) && cc.getComponentType().equals("CALLIBOT") ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.CALLIBOT));
        }
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        motorOnAction.getParam().getSpeed().accept(this);
        String port = motorOnAction.getUserDefinedPort();
        ConfigurationComponent cc = this.robotConfiguration.optConfigurationComponent(port);
        if ( (cc != null) && cc.getComponentType().equals("CALLIBOT") ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.CALLIBOT));
        }
        return null;
    }

    @Override
    public Void visitSingleMotorOnAction(SingleMotorOnAction<Void> singleMotorOnAction) {
        singleMotorOnAction.getSpeed().accept(this);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        ConfigurationComponent cc = this.robotConfiguration.optConfigurationComponent(port);
        if ( (cc != null) && cc.getComponentType().equals("CALLIBOT") ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.CALLIBOT));
        }
        return null;
    }

    @Override
    public Void visitSingleMotorStopAction(SingleMotorStopAction<Void> singleMotorStopAction) {
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(gyroSensor.getPort(), SC.ACCELEROMETER, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Void> fourDigitDisplayShowAction) {
        fourDigitDisplayShowAction.getValue().accept(this);
        fourDigitDisplayShowAction.getPosition().accept(this);
        fourDigitDisplayShowAction.getColon().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.FOUR_DIGIT_DISPLAY));
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Void> fourDigitDisplayClearAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.FOUR_DIGIT_DISPLAY));
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction<Void> ledBarSetAction) {
        ledBarSetAction.getX().accept(this);
        ledBarSetAction.getBrightness().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.LED_BAR));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String port = ultrasonicSensor.getPort();
        ConfigurationComponent cc = this.robotConfiguration.optConfigurationComponent(port);
        if ( (cc != null) && cc.getComponentType().equals("CALLIBOT") ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.CALLIBOT));
        }
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        toneAction.getFrequency().accept(this);
        toneAction.getDuration().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.CALLIBOT));
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPull) {
        return null;
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        bothMotorsOnAction.getSpeedA().accept(this);
        bothMotorsOnAction.getSpeedB().accept(this);
        String portA = bothMotorsOnAction.getPortA();
        ConfigurationComponent cc = this.robotConfiguration.optConfigurationComponent(portA);
        if ( (cc != null) && cc.getComponentType().equals("CALLIBOT") ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.CALLIBOT));
        }
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(humiditySensor.getPort(), SC.HUMIDITY, humiditySensor.getMode()));
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction<Void> switchLedMatrixAction) {
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction<Void> servoSetAction) {
        servoSetAction.getValue().accept(this);
        String port = servoSetAction.getPort();
        ConfigurationComponent cc = this.robotConfiguration.optConfigurationComponent(port);
        if ( (cc != null) && cc.getComponentType().equals("CALLIBOT") ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.CALLIBOT));
        }
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction<Void> motionKitSingleSetAction) {
        return null;
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction<Void> motionKitDualSetAction) {
        return null;
    }
}