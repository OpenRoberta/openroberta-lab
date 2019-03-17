package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
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
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinWriteValue;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IMbedVisitor<V> extends IDisplayVisitor<V>, ILightVisitor<V>, ISoundVisitor<V>, IMotorVisitor<V>, ISensorVisitor<V>, ISerialVisitor<V> {

    /**
     * visit a {@link DisplayTextAction}.
     *
     * @param displayTextAction phrase to be visited
     */
    V visitDisplayTextAction(DisplayTextAction<V> displayTextAction);

    /**
     * visit a {@link PredefinedImage}.
     *
     * @param predefinedImage phrase to be visited
     */
    V visitPredefinedImage(PredefinedImage<V> predefinedImage);

    /**
     * visit a {@link DisplayImageAction}.
     *
     * @param displayImageAction phrase to be visited
     */
    V visitDisplayImageAction(DisplayImageAction<V> displayImageAction);

    /**
     * visit a {@link ImageShiftFunction}.
     *
     * @param imageShiftFunction phrase to be visited
     */
    V visitImageShiftFunction(ImageShiftFunction<V> imageShiftFunction);

    /**
     * visit a {@link ImageShiftFunction}.
     *
     * @param imageShiftFunction phrase to be visited
     */
    V visitImageInvertFunction(ImageInvertFunction<V> imageInvertFunction);

    /**
     * visit a {@link Image}.
     *
     * @param image phrase to be visited
     */
    V visitImage(Image<V> image);

    /**
     * visit a {@link GestureSensor}.
     *
     * @param gestureSensor phrase to be visited
     */
    @Override
    V visitGestureSensor(GestureSensor<V> gestureSensor);

    /**
     * visit a {@link TemperatureSensor}.
     *
     * @param temperatureSensor phrase to be visited
     */
    @Override
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    /**
     * visit a {@link LedOnAction}.
     *
     * @param ledOnAction phrase to be visited
     */
    V visitLedOnAction(LedOnAction<V> ledOnAction);

    /**
     * visit a {@link RadioSendAction}.
     *
     * @param radioSendAction phrase to be visited
     */
    V visitRadioSendAction(RadioSendAction<V> radioSendAction);

    /**
     * visit a {@link RadioReceiveAction}.
     *
     * @param radioReceiveAction phrase to be visited
     */
    V visitRadioReceiveAction(RadioReceiveAction<V> radioReceiveAction);

    /**
     * visit a {@link RgbColor}.
     *
     * @param rgbColor phrase to be visited
     */
    V visitRgbColor(RgbColor<V> rgbColor);

    /**
     * visit a {@link PinGetValueSensor}.
     *
     * @param pinValueSensor phrase to be visited
     */
    @Override
    V visitPinGetValueSensor(PinGetValueSensor<V> pinValueSensor);

    /**
     * visit a {@link PinWriteValue}.
     *
     * @param pinWriteValueSensor phrase to be visited
     */
    V visitPinWriteValueSensor(PinWriteValue<V> pinWriteValueSensor);

    /**
     * visit a {@link PinSetPullAction}.
     *
     * @param pinSetPull phrase to be visited
     */
    V visitPinSetPullAction(PinSetPullAction<V> pinSetPullAction);

    /**
     * visit a {@link DisplaySetBrightnessAction}.
     *
     * @param displaySetBrightnessAction phrase to be visited
     */
    V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<V> displaySetBrightnessAction);

    /**
     * visit a {@link DisplayGetBrightnessAction}.
     *
     * @param displayGetBrightnessAction phrase to be visited
     */
    V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<V> displayGetBrightnessAction);

    /**
     * visit a {@link DisplaySetPixelAction}.
     *
     * @param DisplaySetPixelAction phrase to be visited
     */
    V visitDisplaySetPixelAction(DisplaySetPixelAction<V> displaySetPixelAction);

    /**
     * visit a {@link DisplayGetPixelAction}.
     *
     * @param DisplayGetPixelAction phrase to be visited
     */
    V visitDisplayGetPixelAction(DisplayGetPixelAction<V> displayGetPixelAction);

    /**
     * visit a {@link RadioSetChannelAction}.
     *
     * @param radioSetChannelAction phrase to be visited
     */
    V visitRadioSetChannelAction(RadioSetChannelAction<V> radioSetChannelAction);

    /**
     * visit a {@link SingleMotorOnAction}.
     *
     * @param singleMotorOnAction phrase to be visited
     */
    V visitSingleMotorOnAction(SingleMotorOnAction<V> singleMotorOnAction);

    /**
     * visit a {@link SingleMotorStopAction}.
     *
     * @param singleMotorStopAction phrase to be visited
     */
    V visitSingleMotorStopAction(SingleMotorStopAction<V> singleMotorStopAction);

    /**
     * visit a {@link AccelerometerSensor}.
     *
     * @param accelerometerSensor phrase to be visited
     */
    @Override
    V visitAccelerometer(AccelerometerSensor<V> accelerometerSensor);

    /**
     * visit a {@link GyroSensor}.
     *
     * @param gyroSensor phrase to be visited
     */
    @Override
    V visitGyroSensor(GyroSensor<V> gyroSensor);

    /**
     * visit a {@link visitFourDigitDisplayShowAction}.
     *
     * @param FourDigitDisplayShowAction phrase to be visited
     */
    V visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<V> fourDigitDisplayShowAction);

    /**
     * visit a {@link FourDigitDisplayClearAction}.
     *
     * @param FourDigitDisplayClearAction phrase to be visited
     */
    V visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<V> fourDigitDisplayClearAction);

    V visitBothMotorsOnAction(BothMotorsOnAction<V> bothMotorsOnAction);

    V visitBothMotorsStopAction(BothMotorsStopAction<V> bothMotorsStopAction);

    @Override
    default V visitShowTextAction(ShowTextAction<V> showTextAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitLightAction(LightAction<V> lightAction) {
        throw new DbcException("Not supported!");
    }

    default V visitRadioRssiSensor(RadioRssiSensor<V> radioRssiSensor) {
        throw new DbcException("Not supported!");
    }

    default V visitLedBarSetAction(LedBarSetAction<V> ledBarSetAction) {
        throw new DbcException("Not supported!");
    }

}
