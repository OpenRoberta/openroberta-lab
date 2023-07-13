package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.light.LedsOffAction;
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
import de.fhg.iais.roberta.syntax.action.thymio.LedButtonOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedCircleOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedProxHOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedProxVOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedSoundOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedTemperatureOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.PlayRecordingAction;
import de.fhg.iais.roberta.syntax.action.thymio.RecordStartAction;
import de.fhg.iais.roberta.syntax.action.thymio.RecordStopAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.thymio.TapSensor;

public interface IThymioVisitor<V> extends IVisitor<V> {
    V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    V visitColorConst(ColorConst colorConst);


    V visitCurveAction(CurveAction curveAction);

    V visitDriveAction(DriveAction driveAction);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitLedButtonOnAction(LedButtonOnAction ledButtonOnAction);

    V visitLedCircleOnAction(LedCircleOnAction ledCircleOnAction);

    V visitLedProxHOnAction(LedProxHOnAction ledProxHOnAction);

    V visitLedProxVOnAction(LedProxVOnAction ledProxVOnAction);

    V visitLedSoundOnAction(LedSoundOnAction ledSoundOnAction);

    V visitLedTemperatureOnAction(LedTemperatureOnAction ledTemperatureOnAction);

    V visitLedsOffAction(LedsOffAction ledsOffAction);

    V visitLightAction(LedAction lightAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);

    V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitPlayFileAction(PlayFileAction playFileAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitPlayRecordingAction(PlayRecordingAction playRecordingAction);

    V visitRecordStartAction(RecordStartAction recordStartAction);

    V visitRecordStopAction(RecordStopAction recordStopAction);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitTapSensor(TapSensor tapSensor);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitToneAction(ToneAction toneAction);

    V visitTurnAction(TurnAction turnAction);

    V visitGetVolumeAction(GetVolumeAction getVolumeAction);

    V visitSetVolumeAction(SetVolumeAction setVolumeAction);
}
