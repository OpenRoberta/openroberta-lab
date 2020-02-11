package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.general.IDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
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
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
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
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class MbedStackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements IMbedVisitor<V> {

    public MbedStackMachineVisitor(ConfigurationAst configuration, ArrayList<ArrayList<Phrase<Void>>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());

    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
        int r = colorConst.getRedChannelInt();
        int g = colorConst.getGreenChannelInt();
        int b = colorConst.getBlueChannelInt();

        JSONObject o = mk(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, new JSONArray(Arrays.asList(r, g, b)));
        return app(o);
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        JSONObject o = mk(C.CLEAR_DISPLAY_ACTION);

        return app(o);
    }

    @Override
    public V visitDisplayTextAction(DisplayTextAction<V> displayTextAction) {
        displayTextAction.getMsg().accept(this);
        JSONObject o = mk(C.SHOW_TEXT_ACTION).put(C.MODE, displayTextAction.getMode().toString().toLowerCase());

        return app(o);
    }

    @Override
    public V visitImage(Image<V> image) {
        JSONArray jsonImage = new JSONArray();
        for ( int i = 0; i < 5; i++ ) {
            ArrayList<Integer> a = new ArrayList<>();
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.getImage()[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                a.add(map(Integer.parseInt(pixel), 0, 9, 0, 255));
            }
            jsonImage.put(new JSONArray(a));
        }
        JSONObject o = mk(C.EXPR).put(C.EXPR, image.getKind().getName().toLowerCase());
        o.put(C.VALUE, jsonImage);
        return app(o);
    }

    @Override
    public V visitPredefinedImage(PredefinedImage<V> predefinedImage) {
        final String image = predefinedImage.getImageName().getImageString();
        JSONArray a =
            new JSONArray(
                Arrays.stream(image.split("\\\\n")).map(x -> new JSONArray(Arrays.stream(x.split(",")).mapToInt(Integer::parseInt).toArray())).toArray());

        JSONObject o = mk(C.EXPR).put(C.EXPR, C.IMAGE).put(C.VALUE, a);
        return app(o);

    }

    @Override
    public V visitDisplayImageAction(DisplayImageAction<V> displayImageAction) {
        displayImageAction.getValuesToDisplay().accept(this);
        JSONObject o = mk(C.SHOW_IMAGE_ACTION).put(C.MODE, displayImageAction.getDisplayImageMode().toString().toLowerCase());
        return app(o);
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        JSONObject o = mk(C.STATUS_LIGHT_ACTION).put(C.NAME, "calliope").put(C.PORT, "internal");
        return app(o);
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        toneAction.getFrequency().accept(this);
        toneAction.getDuration().accept(this);
        JSONObject o = mk(C.TONE_ACTION);
        return app(o);
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        String freq = playNoteAction.getFrequency();
        String duration = playNoteAction.getDuration();
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, freq));
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, duration));
        JSONObject o = mk(C.TONE_ACTION);
        return app(o);
    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        return null;
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        motorOnAction.getParam().getSpeed().accept(this);
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));
        String port = motorOnAction.getUserDefinedPort();

        JSONObject o = mk(C.MOTOR_ON_ACTION).put(C.PORT, port.toLowerCase()).put(C.NAME, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        return null;
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        JSONObject o = mk(C.MOTOR_STOP).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitSerialWriteAction(SerialWriteAction<V> serialWriteAction) {
        serialWriteAction.getValue().accept(this);
        JSONObject o = mk(C.SERIAL_WRITE_ACTION);
        return app(o);
    }

    @Override
    public V visitPinWriteValueAction(PinWriteValueAction<V> pinWriteValueAction) {
        pinWriteValueAction.getValue().accept(this);
        String pin = pinWriteValueAction.getPort();
        String mode = pinWriteValueAction.getMode();
        JSONObject o = mk(C.WRITE_PIN_ACTION).put(C.PIN, pin).put(C.MODE, mode.toLowerCase());
        return app(o);
    }

    @Override
    public V visitImageShiftFunction(ImageShiftFunction<V> imageShiftFunction) {
        imageShiftFunction.getImage().accept(this);
        imageShiftFunction.getPositions().accept(this);
        IDirection direction = imageShiftFunction.getShiftDirection();
        JSONObject o = mk(C.IMAGE_SHIFT_ACTION).put(C.DIRECTION, direction.toString().toLowerCase());
        return app(o);
    }

    @Override
    public V visitImageInvertFunction(ImageInvertFunction<V> imageInvertFunction) {
        imageInvertFunction.getImage().accept(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.SINGLE_FUNCTION).put(C.OP, C.IMAGE_INVERT_ACTION);
        return app(o);
    }

    @Override
    public V visitGestureSensor(GestureSensor<V> gestureSensor) {
        String mode = gestureSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GESTURE).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor) {
        String mode = temperatureSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TEMPERATURE).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitKeysSensor(KeysSensor<V> keysSensor) {
        String port = keysSensor.getPort();
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, port).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitLightSensor(LightSensor<V> lightSensor) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.LIGHT).put(C.MODE, C.AMBIENTLIGHT).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        String port = timerSensor.getPort();
        JSONObject o;
        if ( timerSensor.getMode().equals(SC.DEFAULT) || timerSensor.getMode().equals(SC.VALUE) ) {
            o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "calliope");
        } else {
            o = mk(C.TIMER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "calliope");
        }
        return app(o);
    }

    @Override
    public V visitPinTouchSensor(PinTouchSensor<V> sensorGetSample) {
        String port = sensorGetSample.getPort();
        String mode = sensorGetSample.getMode();

        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitSoundSensor(SoundSensor<V> soundSensor) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitCompassSensor(CompassSensor<V> compassSensor) {
        String mode = compassSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COMPASS).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitLedOnAction(LedOnAction<V> ledOnAction) {
        ledOnAction.getLedColor().accept(this);
        JSONObject o = mk(C.LED_ON_ACTION);
        return app(o);
    }

    @Override
    public V visitPinGetValueSensor(PinGetValueSensor<V> pinValueSensor) {
        String port = pinValueSensor.getPort();
        String mode = pinValueSensor.getMode();

        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");

        return app(o);
    }

    @Override
    public V visitPinSetPullAction(PinSetPullAction<V> pinSetPullAction) {
        return null;
    }

    @Override
    public V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<V> displaySetBrightnessAction) {
        displaySetBrightnessAction.getBrightness().accept(this);
        JSONObject o = mk(C.DISPLAY_SET_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<V> displayGetBrightnessAction) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.DISPLAY).put(C.MODE, C.BRIGHTNESS).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitDisplaySetPixelAction(DisplaySetPixelAction<V> displaySetPixelAction) {
        displaySetPixelAction.getX().accept(this);
        displaySetPixelAction.getY().accept(this);
        displaySetPixelAction.getBrightness().accept(this);
        JSONObject o = mk(C.DISPLAY_SET_PIXEL_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public V visitDisplayGetPixelAction(DisplayGetPixelAction<V> displayGetPixelAction) {
        displayGetPixelAction.getX().accept(this);
        displayGetPixelAction.getY().accept(this);
        JSONObject o = mk(C.DISPLAY_GET_PIXEL_BRIGHTNESS_ACTION);
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
        bothMotorsOnAction.getSpeedA().accept(this);
        bothMotorsOnAction.getSpeedB().accept(this);
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));
        JSONObject o =
            mk(C.BOTH_MOTORS_ON_ACTION).put(C.PORT_A, bothMotorsOnAction.getPortA().toLowerCase()).put(C.PORT_B, bothMotorsOnAction.getPortB().toLowerCase());

        return app(o);
    }

    @Override
    public V visitBothMotorsStopAction(BothMotorsStopAction<V> bothMotorsStopAction) {
        JSONObject o = mk(C.MOTOR_STOP).put(C.PORT, "ab");
        return app(o);
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    @Override
    public V visitSwitchLedMatrixAction(SwitchLedMatrixAction<V> switchLedMatrixAction) {
        return null;
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        return null;
    }

    @Override
    public V visitRadioSendAction(RadioSendAction<V> radioSendAction) {
        return null;
    }

    @Override
    public V visitRadioReceiveAction(RadioReceiveAction<V> radioReceiveAction) {
        return null;
    }

    @Override
    public V visitRadioSetChannelAction(RadioSetChannelAction<V> radioSetChannelAction) {
        return null;
    }

    @Override
    public V visitRadioRssiSensor(RadioRssiSensor<V> radioRssiSensor) {
        return null;
    }

    @Override
    public V visitHumiditySensor(HumiditySensor<V> humiditySensor) {
        return null;
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        return null;
    }

    @Override
    public V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
        return null;
    }

    @Override
    public V visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<V> fourDigitDisplayShowAction) {
        return null;
    }

    @Override
    public V visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<V> fourDigitDisplayClearAction) {
        return null;
    }

    @Override
    public V visitLedBarSetAction(LedBarSetAction<V> ledBarSetAction) {
        return null;
    }

    @Override
    public V visitServoSetAction(ServoSetAction<V> servoSetAction) {
        return null;
    }
}
