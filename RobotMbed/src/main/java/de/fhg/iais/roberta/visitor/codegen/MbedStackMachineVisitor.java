package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.general.IDirection;
import de.fhg.iais.roberta.syntax.Phrase;
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
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
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
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IMbedVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class MbedStackMachineVisitor extends AbstractStackMachineVisitor implements IMbedVisitor<Void> {

    public MbedStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());

    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        int r = colorConst.getRedChannelInt();
        int g = colorConst.getGreenChannelInt();
        int b = colorConst.getBlueChannelInt();

        JSONObject o = makeNode(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, new JSONArray(Arrays.asList(r, g, b)));
        return app(o);
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        JSONObject o = makeNode(C.CLEAR_DISPLAY_ACTION);

        return app(o);
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        displayTextAction.msg.accept(this);
        JSONObject o = makeNode(C.SHOW_TEXT_ACTION).put(C.MODE, displayTextAction.mode.toString().toLowerCase());

        return app(o);
    }

    @Override
    public Void visitImage(Image image) {
        JSONArray jsonImage = new JSONArray();
        for ( int i = 0; i < 5; i++ ) {
            ArrayList<Integer> a = new ArrayList<>();
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.image[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                a.add(map(Integer.parseInt(pixel), 0, 9, 0, 255));
            }
            jsonImage.put(new JSONArray(a));
        }
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, image.getKind().getName().toLowerCase());
        o.put(C.VALUE, jsonImage);
        return app(o);
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        final String image = predefinedImage.getImageName().getImageString();
        JSONArray a =
            new JSONArray(
                Arrays.stream(image.split("\\\\n")).map(x -> new JSONArray(Arrays.stream(x.split(",")).mapToInt(Integer::parseInt).toArray())).toArray());

        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.IMAGE).put(C.VALUE, a);
        return app(o);

    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        displayImageAction.getValuesToDisplay().accept(this);
        JSONObject o = makeNode(C.SHOW_IMAGE_ACTION).put(C.MODE, displayImageAction.displayImageMode.toString().toLowerCase());
        return app(o);
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        JSONObject o = makeNode(C.STATUS_LIGHT_ACTION).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        toneAction.frequency.accept(this);
        toneAction.duration.accept(this);
        JSONObject o = makeNode(C.TONE_ACTION);
        return app(o);
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        String freq = playNoteAction.frequency;
        String duration = playNoteAction.duration;
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, freq));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, duration));
        JSONObject o = makeNode(C.TONE_ACTION);
        return app(o);
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        motorOnAction.param.getSpeed().accept(this);
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));

        String port = motorOnAction.getUserDefinedPort();
        ConfigurationComponent cc = this.configuration.optConfigurationComponent(port);
        String pin1 = ((cc == null) || cc.componentType.equals("CALLIBOT")) ? "0" : cc.getProperty("PIN1");

        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, pin1.toLowerCase()).put(C.NAME, pin1.toLowerCase());
        return app(o);
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        ConfigurationComponent cc = this.configuration.optConfigurationComponent(port);
        String pin1 = ((cc == null) || cc.componentType.equals("CALLIBOT")) ? "0" : cc.getProperty("PIN1");

        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, pin1.toLowerCase());
        return app(o);
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        pinWriteValueAction.value.accept(this);
        String port = pinWriteValueAction.port;
        ConfigurationComponent cc = this.configuration.optConfigurationComponent(port);
        String pin1 = (cc == null) ? "0" : cc.getProperty("PIN1");

        String mode = pinWriteValueAction.pinValue;
        JSONObject o = makeNode(C.WRITE_PIN_ACTION).put(C.PIN, pin1).put(C.MODE, mode.toLowerCase());
        return app(o);
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        imageShiftFunction.image.accept(this);
        imageShiftFunction.positions.accept(this);
        IDirection direction = imageShiftFunction.shiftDirection;
        JSONObject o = makeNode(C.IMAGE_SHIFT_ACTION).put(C.DIRECTION, direction.toString().toLowerCase());
        return app(o);
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        imageInvertFunction.image.accept(this);
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.SINGLE_FUNCTION).put(C.OP, C.IMAGE_INVERT_ACTION);
        return app(o);
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        String mode = gestureSensor.getMode();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GESTURE).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        String mode = temperatureSensor.getMode();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TEMPERATURE).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String port = keysSensor.getUserDefinedPort();
        ConfigurationComponent cc = this.configuration.optConfigurationComponent(port);
        String pin1 = (cc == null) ? "0" : cc.getProperty("PIN1");

        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, pin1).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.LIGHT).put(C.MODE, C.AMBIENTLIGHT).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String port = timerSensor.getUserDefinedPort();
        JSONObject o;
        if ( timerSensor.getMode().equals(SC.DEFAULT) || timerSensor.getMode().equals(SC.VALUE) ) {
            o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "calliope");
        } else {
            o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "calliope");
        }
        return app(o);
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor sensorGetSample) {
        String port = sensorGetSample.getUserDefinedPort();
        String mode = sensorGetSample.getMode();

        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        String mode = compassSensor.getMode();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COMPASS).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public Void visitLedOnAction(LedOnAction ledOnAction) {
        ledOnAction.ledColor.accept(this);
        JSONObject o = makeNode(C.LED_ON_ACTION);
        return app(o);
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        String port = pinValueSensor.getUserDefinedPort();
        ConfigurationComponent cc = this.configuration.optConfigurationComponent(port);
        String pin1 = (cc == null) ? "0" : cc.getProperty("PIN1");
        String mode = pinValueSensor.getMode();

        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + pin1).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");

        return app(o);
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        displaySetBrightnessAction.brightness.accept(this);
        JSONObject o = makeNode(C.DISPLAY_SET_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.DISPLAY).put(C.MODE, C.BRIGHTNESS).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        displaySetPixelAction.x.accept(this);
        displaySetPixelAction.y.accept(this);
        displaySetPixelAction.brightness.accept(this);
        JSONObject o = makeNode(C.DISPLAY_SET_PIXEL_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        displayGetPixelAction.x.accept(this);
        displayGetPixelAction.y.accept(this);
        JSONObject o = makeNode(C.DISPLAY_GET_PIXEL_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        return null;
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        bothMotorsOnAction.speedA.accept(this);
        bothMotorsOnAction.speedB.accept(this);

        String portA = bothMotorsOnAction.portA;
        ConfigurationComponent ccA = this.configuration.optConfigurationComponent(portA);
        String pin1A = ((ccA == null) || ccA.componentType.equals("CALLIBOT")) ? "0" : ccA.getProperty("PIN1");
        String portB = bothMotorsOnAction.portB;
        ConfigurationComponent ccB = this.configuration.optConfigurationComponent(portB);
        String pin1B = ((ccB == null) || ccB.componentType.equals("CALLIBOT")) ? "0" : ccB.getProperty("PIN1");

        JSONObject o = makeNode(C.BOTH_MOTORS_ON_ACTION).put(C.PORT_A, pin1A.toLowerCase()).put(C.PORT_B, pin1B.toLowerCase());

        return app(o);
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, "ab");
        return app(o);
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction servoSetAction) {
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        return null;
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        return null;
    }
}
