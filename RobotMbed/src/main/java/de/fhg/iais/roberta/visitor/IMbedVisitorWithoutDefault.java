package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DcMotorSetAction;
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
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISimpleSoundVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IMbedVisitorWithoutDefault<V>
        extends IDisplayVisitor<V>, ILightVisitor<V>, ISimpleSoundVisitor<V>, IMotorVisitor<V>, IPinVisitor<V> {

    V visitDisplayTextAction(DisplayTextAction displayTextAction);

    V visitPredefinedImage(PredefinedImage predefinedImage);

    V visitDisplayImageAction(DisplayImageAction displayImageAction);

    V visitImageShiftFunction(ImageShiftFunction imageShiftFunction);

    V visitImageInvertFunction(ImageInvertFunction imageInvertFunction);

    V visitImage(Image image);

    V visitLedOnAction(LedOnAction ledOnAction);

    V visitRadioSendAction(RadioSendAction radioSendAction);

    V visitRadioReceiveAction(RadioReceiveAction radioReceiveAction);

    V visitPinSetPullAction(PinSetPullAction pinSetPullAction);

    V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction);

    V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction);

    V visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction);

    V visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction);

    V visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction);

    @Deprecated
    V visitSingleMotorOnAction(SingleMotorOnAction singleMotorOnAction);

    @Deprecated
    V visitSingleMotorStopAction(SingleMotorStopAction singleMotorStopAction);

    V visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction);

    V visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction);

    V visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction);

    V visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction);

    V visitRadioRssiSensor(RadioRssiSensor radioRssiSensor);

    V visitLedBarSetAction(LedBarSetAction ledBarSetAction);

    V visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction);

    V visitServoSetAction(ServoSetAction servoSetAction);

    V visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction);

    V visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction);



    V visitKeysSensor(KeysSensor keysSensor);

    V visitColorSensor(ColorSensor colorSensor);

    V visitLightSensor(LightSensor lightSensor);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitCompassSensor(CompassSensor compassSensor);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    V visitPinTouchSensor(PinTouchSensor pinTouchSensor);

    V visitGestureSensor(GestureSensor gestureSensor);

    V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor);

    V visitGetSampleSensor(GetSampleSensor sensorGetSample);

    V visitHumiditySensor(HumiditySensor humiditySensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitDcMotorSetAction(DcMotorSetAction dcMotorSetAction);
}