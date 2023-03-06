package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IMbedVisitor<V> extends IVisitor<V> {
    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitDisplayTextAction(DisplayTextAction displayTextAction);

    V visitPredefinedImage(PredefinedImage predefinedImage);

    V visitDisplayImageAction(DisplayImageAction displayImageAction);

    V visitImageShiftFunction(ImageShiftFunction imageShiftFunction);

    V visitImageInvertFunction(ImageInvertFunction imageInvertFunction);

    V visitImage(Image image);

    V visitRadioSendAction(RadioSendAction radioSendAction);

    V visitRadioReceiveAction(RadioReceiveAction radioReceiveAction);

    V visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction);

    V visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction);

    V visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction);

    V visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    V visitLightSensor(LightSensor lightSensor);

    V visitGestureSensor(GestureSensor gestureSensor);

    V visitCompassSensor(CompassSensor compassSensor);

    V visitPinTouchSensor(PinTouchSensor pinTouchSensor);

    V visitPinGetValueSensor(PinGetValueSensor pinValueSensor);

    V visitToneAction(ToneAction toneAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        sensorGetSample.sensor.accept(this);
        return null;
    }

    @Override
    V visit(Phrase visitable);
}