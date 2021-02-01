package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.mbed.*;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.*;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IMbedVisitorWithoutDefault<V>
    extends IDisplayVisitor<V>, ILightVisitor<V>, ISimpleSoundVisitor<V>, IMotorVisitor<V>, ISerialVisitor<V>, IPinVisitor<V> {

    V visitDisplayTextAction(DisplayTextAction<V> displayTextAction);

    V visitPredefinedImage(PredefinedImage<V> predefinedImage);

    V visitDisplayImageAction(DisplayImageAction<V> displayImageAction);

    V visitImageShiftFunction(ImageShiftFunction<V> imageShiftFunction);

    V visitImageInvertFunction(ImageInvertFunction<V> imageInvertFunction);

    V visitImage(Image<V> image);

    V visitLedOnAction(LedOnAction<V> ledOnAction);

    V visitRadioSendAction(RadioSendAction<V> radioSendAction);

    V visitRadioReceiveAction(RadioReceiveAction<V> radioReceiveAction);

    V visitPinSetPullAction(PinSetPullAction<V> pinSetPullAction);

    V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<V> displaySetBrightnessAction);

    V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<V> displayGetBrightnessAction);

    V visitDisplaySetPixelAction(DisplaySetPixelAction<V> displaySetPixelAction);

    V visitDisplayGetPixelAction(DisplayGetPixelAction<V> displayGetPixelAction);

    V visitRadioSetChannelAction(RadioSetChannelAction<V> radioSetChannelAction);

    @Deprecated
    V visitSingleMotorOnAction(SingleMotorOnAction<V> singleMotorOnAction);

    @Deprecated
    V visitSingleMotorStopAction(SingleMotorStopAction<V> singleMotorStopAction);

    V visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<V> fourDigitDisplayShowAction);

    V visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<V> fourDigitDisplayClearAction);

    V visitBothMotorsOnAction(BothMotorsOnAction<V> bothMotorsOnAction);

    V visitBothMotorsStopAction(BothMotorsStopAction<V> bothMotorsStopAction);

    V visitRadioRssiSensor(RadioRssiSensor<V> radioRssiSensor);

    V visitLedBarSetAction(LedBarSetAction<V> ledBarSetAction);

    V visitSwitchLedMatrixAction(SwitchLedMatrixAction<V> switchLedMatrixAction);

    V visitServoSetAction(ServoSetAction<V> servoSetAction);

    V visitMotionKitSingleSetAction(MotionKitSingleSetAction<V> motionKitSingleSetAction);

    V visitMotionKitDualSetAction(MotionKitDualSetAction<V> motionKitDualSetAction);



    V visitKeysSensor(KeysSensor<V> keysSensor);

    V visitColorSensor(ColorSensor<V> colorSensor);

    V visitLightSensor(LightSensor<V> lightSensor);

    V visitSoundSensor(SoundSensor<V> soundSensor);

    V visitGyroSensor(GyroSensor<V> gyroSensor);

    V visitTimerSensor(TimerSensor<V> timerSensor);

    V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor);

    V visitCompassSensor(CompassSensor<V> compassSensor);

    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitAccelerometerSensor(AccelerometerSensor<V> accelerometerSensor);

    V visitPinTouchSensor(PinTouchSensor<V> pinTouchSensor);

    V visitGestureSensor(GestureSensor<V> gestureSensor);

    V visitPinGetValueSensor(PinGetValueSensor<V> pinGetValueSensor);

    V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample);

    V visitHumiditySensor(HumiditySensor<V> humiditySensor);

    V visitInfraredSensor(InfraredSensor<V> infraredSensor);
}