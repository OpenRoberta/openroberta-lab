package de.fhg.iais.roberta.visitor;


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
import de.fhg.iais.roberta.syntax.colour.ColourCompare;
import de.fhg.iais.roberta.syntax.sensor.CameraBallSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineColourSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineInformationSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineSensor;
import de.fhg.iais.roberta.syntax.sensor.EnvironmentalCalibrate;
import de.fhg.iais.roberta.syntax.sensor.Phototransistor;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchKeySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;

public interface ITxt4Visitor<V> extends IVisitor<V> {

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorOmniDiffOnAction(MotorOmniDiffOnAction motorOmniDiffOnAction);

    V visitMotorOmniDiffOnForAction(MotorOmniDiffOnForAction motorOmniDiffOnForAction);

    V visitMotorOmniDiffCurveAction(MotorOmniDiffCurveAction motorOmniDiffCurveAction);

    V visitMotorOmniDiffCurveForAction(MotorOmniDiffCurveForAction motorOmniDiffCurveForAction);

    V visitMotorOmniDiffTurnAction(MotorOmniDiffTurnAction motorOmniDiffTurnAction);

    V visitMotorOmniDiffTurnForAction(MotorOmniDiffTurnForAction motorOmniDiffTurnForAction);

    V visitMotorOnForAction(MotorOnForAction motorOnForAction);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitEncoderReset(EncoderReset encoderReset);

    V visitServoOnForAction(ServoOnForAction servoOnForAction);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitTouchKeySensor(TouchKeySensor touchKeySensor);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitMotorOmniDiffStopAction(MotorOmniDiffStopAction motorOmniDiffStopAction);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitLedBrightnessAction(LedBrightnessAction ledBrightnessAction);

    V visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction);

    V visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction);

    V visitDisplayTextAction(DisplayTextAction displayTextAction);

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitMotionSensor(MotionSensor motionSensor);

    V visitColorSensor(ColorSensor colorSensor);

    V visitCameraLineSensor(CameraLineSensor cameraLineSensor);

    V visitCameraLineInformationSensor(CameraLineInformationSensor cameraLineInformationSensor);

    V visitCameraLineColourSensor(CameraLineColourSensor cameraLineColourSensor);

    V visitCameraBallSensor(CameraBallSensor cameraBallSensor);

    V visitColourCompare(ColourCompare colourCompare);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitLightSensor(LightSensor lightSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    V visitCompassSensor(CompassSensor compassSensor);

    V visitGestureSensor(GestureSensor gestureSensor);

    V visitEnvironmentalSensor(EnvironmentalSensor environmentalSensor);

    V visitEnvironmentalCalibrate(EnvironmentalCalibrate environmentalCalibrate);

    V visitPlayFileAction(PlayFileAction playFileAction);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitPhototransistor(Phototransistor phototransistor);
}
