package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.C;

public class MbedStackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements IMbedVisitor<V> {

    private MbedStackMachineVisitor(Configuration configuration, ArrayList<ArrayList<Phrase<Void>>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());

    }

    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        Assert.notNull(brickConfiguration);

        MbedStackMachineVisitor<Void> astVisitor = new MbedStackMachineVisitor<>(brickConfiguration, phrasesSet);
        astVisitor.generateCodeFromPhrases(phrasesSet);
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, astVisitor.opArray).put(C.FUNCTION_DECLARATION, astVisitor.fctDecls);
        return generatedCode.toString(2);
    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
        String color = colorConst.getHexValueAsString().toUpperCase();
        JSONObject o = mk(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, color);
        return app(o);
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        JSONObject o = mk(C.CLEAR_DISPLAY_ACTION);

        return app(o);
    }

    @Override
    public V visitDisplayTextAction(DisplayTextAction<V> displayTextAction) {
        displayTextAction.getMsg().visit(this);
        JSONObject o = mk(C.DISPLAY_TEXT_ACTION).put(C.MODE, displayTextAction.getMode());

        return app(o);
    }

    @Override
    public V visitImage(Image<V> image) {
        String imageString = "";
        for ( int i = 0; i < 5; i++ ) {
            imageString += "[";
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.getImage()[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                imageString += map(Integer.parseInt(pixel), 0, 9, 0, 255);
                if ( j < 4 ) {
                    imageString += ",";
                }
            }
            imageString += "]";
            if ( i < 4 ) {
                imageString += ",";
            }
        }
        JSONObject o = mk(C.EXPR).put(C.EXPR, image.getKind().getName());
        o.put(C.VALUE, imageString);
        return app(o);
    }

    @Override
    public V visitPredefinedImage(PredefinedImage<V> predefinedImage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V visitDisplayImageAction(DisplayImageAction<V> displayImageAction) {
        displayImageAction.getValuesToDisplay().visit(this);
        JSONObject o = mk(C.DISPLAY_IMAGE_ACTION).put(C.MODE, displayImageAction.getDisplayImageMode());
        return app(o);
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        JSONObject o = mk(C.LED_OFF_ACTION);
        return app(o);
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        toneAction.getFrequency().visit(this);
        toneAction.getDuration().visit(this);
        JSONObject o = mk(C.TONE_ACTION);
        return app(o);
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        String duration = playNoteAction.getDuration();
        String freq = playNoteAction.getFrequency();
        JSONObject o = mk(C.PLAY_NOTE_ACTION).put(C.DURATION, duration).put(C.FREQUENCY, freq);
        return app(o);
    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        return null;
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        motorOnAction.getParam().getSpeed().visit(this);
        String port = motorOnAction.getUserDefinedPort();
        JSONObject o = mk(C.MOTOR_ON_ACTION).put(C.PORT.toLowerCase(), port);
        return app(o);
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        return null;
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        JSONObject o = mk(C.MOTOR_STOP).put(C.PORT.toLowerCase(), port);
        return app(o);
    }

    @Override
    public V visitSerialWriteAction(SerialWriteAction<V> serialWriteAction) {
        serialWriteAction.getValue().visit(this);
        JSONObject o = mk(C.SERIAL_WRITE_ACTION);
        return app(o);
    }

    @Override
    public V visitPinWriteValueAction(PinWriteValueAction<V> pinWriteValueAction) {
        pinWriteValueAction.getValue().visit(this);
        String pin = pinWriteValueAction.getPort();
        String mode = pinWriteValueAction.getMode();
        JSONObject o = mk(C.WRITE_PIN_ACTION).put(C.PIN, pin).put(C.MODE, mode);
        return app(o);
    }

    @Override
    public V visitImageShiftFunction(ImageShiftFunction<V> imageShiftFunction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V visitImageInvertFunction(ImageInvertFunction<V> imageInvertFunction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V visitGestureSensor(GestureSensor<V> gestureSensor) {
        String mode = gestureSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE).put(C.PORT.toLowerCase(), mode).put(C.SENSOR_TYPE, C.GESTURE);
        return app(o);
    }

    @Override
    public V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.SENSOR_TYPE, C.TEMPERATURE);
        return app(o);
    }

    @Override
    public V visitKeysSensor(KeysSensor<V> keysSensor) {
        String port = keysSensor.getPort();
        JSONObject o = mk(C.GET_SAMPLE).put(C.PORT.toLowerCase(), port).put(C.SENSOR_TYPE, C.BUTTONS);
        return app(o);
    }

    @Override
    public V visitLightSensor(LightSensor<V> lightSensor) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.SENSOR_TYPE, C.LIGHT);
        return app(o);
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        String port = timerSensor.getPort();
        String mode = C.TIMER_SENSOR_RESET;
        if ( timerSensor.getMode().equals(SC.DEFAULT) || timerSensor.getMode().equals(SC.VALUE) ) {
            mode = C.TIMER;
        }
        JSONObject o = mk(C.GET_SAMPLE).put(C.PORT.toLowerCase(), port).put(C.SENSOR_TYPE, mode);
        return app(o);
    }

    @Override
    public V visitPinTouchSensor(PinTouchSensor<V> sensorGetSample) {
        String port = sensorGetSample.getPort();
        JSONObject o = mk(C.GET_SAMPLE).put(C.PORT.toLowerCase(), port).put(C.SENSOR_TYPE, C.PIN);
        return app(o);
    }

    @Override
    public V visitSoundSensor(SoundSensor<V> soundSensor) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.SENSOR_TYPE, C.SOUND);
        return app(o);
    }

    @Override
    public V visitCompassSensor(CompassSensor<V> compassSensor) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.SENSOR_TYPE, C.COMPASS);
        return app(o);
    }

    @Override
    public V visitLedOnAction(LedOnAction<V> ledOnAction) {
        ledOnAction.getLedColor().visit(this);
        JSONObject o = mk(C.LED_ON_ACTION);
        return app(o);
    }

    @Override
    public V visitPinGetValueSensor(PinGetValueSensor<V> pinValueSensor) {
        String port = pinValueSensor.getPort();
        String mode = pinValueSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE).put(C.PORT.toLowerCase(), port).put(C.MODE, mode).put(C.SENSOR_TYPE, C.PIN);
        return app(o);
    }

    @Override
    public V visitPinSetPullAction(PinSetPullAction<V> pinSetPullAction) {
        return null;
    }

    @Override
    public V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<V> displaySetBrightnessAction) {
        displaySetBrightnessAction.getBrightness().visit(this);
        JSONObject o = mk(C.DISPLAY_SET_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<V> displayGetBrightnessAction) {
        JSONObject o = mk(C.DISPLAY_GET_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public V visitDisplaySetPixelAction(DisplaySetPixelAction<V> displaySetPixelAction) {
        displaySetPixelAction.getX().visit(this);
        displaySetPixelAction.getY().visit(this);
        displaySetPixelAction.getBrightness().visit(this);
        JSONObject o = mk(C.DISPLAY_SET_PIXEL_ACTION);
        return app(o);
    }

    @Override
    public V visitDisplayGetPixelAction(DisplayGetPixelAction<V> displayGetPixelAction) {
        JSONObject o = mk(C.DISPLAY_GET_PIXEL_ACTION);
        return app(o);
    }

    @Override
    public V visitSingleMotorOnAction(SingleMotorOnAction<V> singleMotorOnAction) {

        return null;
    }

    @Override
    public V visitSingleMotorStopAction(SingleMotorStopAction<V> singleMotorStopAction) {

        return null;
    }

    @Override
    public V visitAccelerometer(AccelerometerSensor<V> accelerometerSensor) {
        return null;
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        return null;
    }

    @Override
    public V visitBothMotorsOnAction(BothMotorsOnAction<V> bothMotorsOnAction) {
        bothMotorsOnAction.getSpeedA().visit(this);
        bothMotorsOnAction.getSpeedB().visit(this);
        JSONObject o = mk(C.BOTH_MOTORS_ON_ACTION);
        return app(o);
    }

    @Override
    public V visitBothMotorsStopAction(BothMotorsStopAction<V> bothMotorsStopAction) {
        JSONObject o = mk(C.BOTH_MOTORS_STOP_ACTION);
        return app(o);
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (((x - in_min) * (out_max - out_min)) / (in_max - in_min)) + out_min;
    }

}
