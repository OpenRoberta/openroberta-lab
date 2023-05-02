package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LedsOffAction;
import de.fhg.iais.roberta.syntax.action.mbot2.CommunicationReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbot2.CommunicationSendAction;
import de.fhg.iais.roberta.syntax.action.mbot2.DisplaySetColourAction;
import de.fhg.iais.roberta.syntax.action.mbot2.LedBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbot2.LedOnActionWithIndex;
import de.fhg.iais.roberta.syntax.action.mbot2.PlayRecordingAction;
import de.fhg.iais.roberta.syntax.action.mbot2.PrintlnAction;
import de.fhg.iais.roberta.syntax.action.mbot2.QuadRGBLightOffAction;
import de.fhg.iais.roberta.syntax.action.mbot2.QuadRGBLightOnAction;
import de.fhg.iais.roberta.syntax.action.mbot2.Ultrasonic2LEDAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbot2.GyroResetAxis;
import de.fhg.iais.roberta.syntax.sensor.mbot2.Joystick;
import de.fhg.iais.roberta.syntax.sensor.mbot2.QuadRGBSensor;
import de.fhg.iais.roberta.syntax.sensor.mbot2.SoundRecord;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IMbot2Visitor<V> extends IVisitor<V> {
    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitMainTask(MainTask mainTask);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitJoystick(Joystick joystick);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitEncoderReset(EncoderReset encoderReset);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitSoundRecord(SoundRecord soundRecord);

    V visitLightSensor(LightSensor lightSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitGyroResetAxis(GyroResetAxis gyroResetAxis);

    V visitShowTextAction(ShowTextAction showTextAction);

    V visitPlayRecordingAction(PlayRecordingAction playRecordingAction);

    V visitDisplaySetColourAction(DisplaySetColourAction displaySetColourAction);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitQuadRGBSensor(QuadRGBSensor quadRGBSensor);

    V visitQuadRGBLightOnAction(QuadRGBLightOnAction quadRGBLightOnAction);

    V visitQuadRGBLightOffAction(QuadRGBLightOffAction quadRGBLightOffAction);

    V visitUltrasonic2LEDAction(Ultrasonic2LEDAction ultrasonic2LEDAction);

    V visitLedsOffAction(LedsOffAction ledsOffAction);

    V visitLedBrightnessAction(LedBrightnessAction ledBrightnessAction);

    V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitPrintlnAction(PrintlnAction printlnAction);

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitLedOnActionWithIndex(LedOnActionWithIndex ledOnActionWithIndex);

    V visitCommunicationSendAction(CommunicationSendAction communicationSendAction);

    V visitCommunicationReceiveAction(CommunicationReceiveAction communicationReceiveAction);

    V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitDriveAction(DriveAction driveAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);

    V visitTurnAction(TurnAction turnAction);

    V visitCurveAction(CurveAction curveAction);

    V visitToneAction(ToneAction toneAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitGetVolumeAction(GetVolumeAction getVolumeAction);

    V visitSetVolumeAction(SetVolumeAction setVolumeAction);

    V visitConnectConst(ConnectConst connectConst);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitPlayFileAction(PlayFileAction playFileAction);

    V visitColorConst(ColorConst colorConst);

    V visitRgbColor(RgbColor rgbColor);
}