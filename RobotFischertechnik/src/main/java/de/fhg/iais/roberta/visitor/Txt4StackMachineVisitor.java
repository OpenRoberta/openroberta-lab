package de.fhg.iais.roberta.visitor;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffStopAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.ServoOnForAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.light.LedBrightnessAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.logic.ColourCompare;
import de.fhg.iais.roberta.syntax.sensor.CameraBallSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineColourSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineInformationSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineSensor;
import de.fhg.iais.roberta.syntax.sensor.EnvironmentalCalibrate;
import de.fhg.iais.roberta.syntax.sensor.TouchKeySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class Txt4StackMachineVisitor extends AbstractStackMachineVisitor implements ITxt4Visitor<Void> {

    public Txt4StackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        super(configuration, usedHardwareBean, nnBean);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        motorOnAction.power.accept(this);
        String name = motorOnAction.getUserDefinedPort();
        String port = this.configuration.getConfigurationComponent(name).componentProperties.get("PORT");
        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, port).put(C.NAME, name).put(C.SPEED_ONLY, true);
        return add(o);
    }

    @Override
    public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        motorOnForAction.power.accept(this);
        motorOnForAction.value.accept(this);
        String name = motorOnForAction.getUserDefinedPort();
        String port = this.configuration.getConfigurationComponent(name).componentProperties.get("PORT");
        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, port).put(C.NAME, name).put(C.SPEED_ONLY, false);
        o.put(C.MOTOR_DURATION, motorOnForAction.unit.toLowerCase());
        add(o);
        return add(makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase()));
    }

    @Override
    public Void visitMotorOmniDiffOnAction(MotorOmniDiffOnAction motorOmniDiffOnAction) {
        motorOmniDiffOnAction.power.accept(this);
        String direction = motorOmniDiffOnAction.direction.equals("FORWARD") ? C.FOREWARD : C.BACKWARD;
        JSONObject o =
            makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, direction).put(C.SPEED_ONLY, true).put(C.SET_TIME, false);
        return add(o);
    }

    @Override
    public Void visitMotorOmniDiffOnForAction(MotorOmniDiffOnForAction motorOmniDiffOnForAction) {
        motorOmniDiffOnForAction.power.accept(this);
        motorOmniDiffOnForAction.distance.accept(this);
        String direction = motorOmniDiffOnForAction.direction.equals("FORWARD") ? C.FOREWARD : C.BACKWARD;
        JSONObject o =
            makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, direction).put(C.SPEED_ONLY, false).put(C.SET_TIME, false);
        add(o);
        return add(makeNode(C.STOP_DRIVE));
    }

    @Override
    public Void visitMotorOmniDiffCurveAction(MotorOmniDiffCurveAction motorOmniDiffCurveAction) {
        motorOmniDiffCurveAction.powerLeft.accept(this);
        motorOmniDiffCurveAction.powerRight.accept(this);
        String direction = motorOmniDiffCurveAction.direction.equals("FORWARD") ? C.FOREWARD : C.BACKWARD;
        JSONObject o =
            makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, direction).put(C.SPEED_ONLY, true).put(C.SET_TIME, false);
        return add(o);
    }

    @Override
    public Void visitMotorOmniDiffCurveForAction(MotorOmniDiffCurveForAction motorOmniDiffCurveForAction) {
        motorOmniDiffCurveForAction.powerLeft.accept(this);
        motorOmniDiffCurveForAction.powerRight.accept(this);
        motorOmniDiffCurveForAction.distance.accept(this);
        String direction = motorOmniDiffCurveForAction.direction.equals("FORWARD") ? C.FOREWARD : C.BACKWARD;
        JSONObject o =
            makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, direction).put(C.SPEED_ONLY, false).put(C.SET_TIME, false);
        add(o);
        return add(makeNode(C.STOP_DRIVE));
    }

    @Override
    public Void visitMotorOmniDiffTurnAction(MotorOmniDiffTurnAction motorOmniDiffTurnAction) {
        motorOmniDiffTurnAction.power.accept(this);
        JSONObject o =
            makeNode(C.TURN_ACTION)
                .put(C.TURN_DIRECTION, motorOmniDiffTurnAction.direction.toLowerCase())
                .put(C.SPEED_ONLY, true)
                .put(C.SET_TIME, false);
        return add(o);
    }

    @Override
    public Void visitMotorOmniDiffTurnForAction(MotorOmniDiffTurnForAction motorOmniDiffTurnForAction) {
        motorOmniDiffTurnForAction.power.accept(this);
        motorOmniDiffTurnForAction.degrees.accept(this);
        JSONObject o =
            makeNode(C.TURN_ACTION)
                .put(C.TURN_DIRECTION, motorOmniDiffTurnForAction.direction.toLowerCase())
                .put(C.SPEED_ONLY, false)
                .put(C.SET_TIME, false);
        add(o);
        return add(makeNode(C.STOP_DRIVE));
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        // TODO
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        // TODO
        return null;
    }

    @Override
    public Void visitServoOnForAction(ServoOnForAction servoOnForAction) {
        // not supported
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String mode = ultrasonicSensor.getMode();
        String port = ultrasonicSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.PORT, port).put(C.MODE, mode.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        // not supported
        return null;
    }

    @Override
    public Void visitTouchKeySensor(TouchKeySensor touchKeySensor) {
        String port = "txt4Button";
        port += touchKeySensor.getUserDefinedPort().toLowerCase().substring(0, 1).toUpperCase() + touchKeySensor.getUserDefinedPort().toLowerCase().substring(1);
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String name = motorStopAction.getUserDefinedPort();
        String port = this.configuration.getConfigurationComponent(name).componentProperties.get("PORT");
        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitMotorOmniDiffStopAction(MotorOmniDiffStopAction motorOmniDiffStopAction) {
        JSONObject o = makeNode(C.STOP_DRIVE);
        return add(o);
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
        String slot = infraredSensor.getSlot().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.PORT, port).put(C.SLOT, slot);
        return add(o);
    }

    @Override
    public Void visitLedBrightnessAction(LedBrightnessAction ledBrightnessAction) {
        // not supported
        return null;
    }

    @Override
    public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction displayLedOnAction) {
        String mode = SC.DISPLAY;
        displayLedOnAction.colour.accept(this);
        JSONObject o = makeNode(C.RGBLED_ON_ACTION).put(C.MODE, mode);
        return add(o);
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        int r = colorConst.getRedChannelInt();
        int g = colorConst.getGreenChannelInt();
        int b = colorConst.getBlueChannelInt();

        JSONObject o = makeNode(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, new JSONArray(Arrays.asList(r, g, b)));
        return add(o);
    }

    @Override
    public Void visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction) {
        JSONObject o = makeNode(C.RGBLED_OFF_ACTION);
        return add(o);
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        displayTextAction.row.accept(this);
        displayTextAction.text.accept(this);
        JSONObject o = makeNode(C.SHOW_TEXT_ACTION).put(C.NAME, "txt4");
        return add(o);
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        JSONObject o = makeNode(C.CLEAR_DISPLAY_ACTION);
        return add(o);
    }

    @Override
    public Void visitMotionSensor(MotionSensor motionSensor) {
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.CAMERA).put(C.MODE, C.COLOR);
        return add(o);
    }

    @Override
    public Void visitCameraLineSensor(CameraLineSensor cameraLineSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.CAMERA).put(C.MODE, C.LINE).put(C.SLOT, C.NUMBER);
        return add(o);
    }

    @Override
    public Void visitCameraLineInformationSensor(CameraLineInformationSensor cameraLineInformationSensor) {
        //cameraLineInformationSensor.lineId.accept(this);
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.CAMERA).put(C.MODE, C.LINE).put(C.SLOT, C.INFO);
        return add(o);
    }

    @Override
    public Void visitCameraLineColourSensor(CameraLineColourSensor cameraLineColourSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.CAMERA).put(C.MODE, C.LINE).put(C.SLOT, C.COLOR);
        return add(o);
    }

    @Override
    public Void visitCameraBallSensor(CameraBallSensor cameraBallSensor) {
        return null;
    }

    @Override
    public Void visitColourCompare(ColourCompare colourCompare) {
        colourCompare.colour1.accept(this);
        colourCompare.colour2.accept(this);
        colourCompare.tolerance.accept(this);
        JSONObject o = makeNode(C.COLOUR_COMPARE);
        return add(o);
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String port = timerSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String port = timerReset.sensorPort;
        JSONObject o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        return null;
    }

    @Override
    public Void visitEnvironmentalSensor(EnvironmentalSensor environmentalSensor) {
        return null;
    }

    @Override
    public Void visitEnvironmentalCalibrate(EnvironmentalCalibrate environmentalCalibrate) {
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        return null;
    }
}
