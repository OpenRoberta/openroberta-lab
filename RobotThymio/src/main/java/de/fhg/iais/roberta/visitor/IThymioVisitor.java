package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.light.LedsOffAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.thymio.TapSensor;

public interface IThymioVisitor<V> extends IVisitor<V> {

    default Void visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        sensorGetSample.sensor.accept(this);
        return null;
    }

    Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    Void visitColorConst(ColorConst colorConst);


    Void visitCurveAction(CurveAction curveAction);

    Void visitDriveAction(DriveAction driveAction);

    Void visitInfraredSensor(InfraredSensor infraredSensor);

    Void visitKeysSensor(KeysSensor keysSensor);

    Void visitLedButtonOnAction(LedButtonOnAction ledButtonOnAction);

    Void visitLedCircleOnAction(LedCircleOnAction ledCircleOnAction);

    Void visitLedProxHOnAction(LedProxHOnAction ledProxHOnAction);

    Void visitLedProxVOnAction(LedProxVOnAction ledProxVOnAction);

    Void visitLedSoundOnAction(LedSoundOnAction ledSoundOnAction);

    Void visitLedTemperatureOnAction(LedTemperatureOnAction ledTemperatureOnAction);

    Void visitLedsOffAction(LedsOffAction ledsOffAction);

    Void visitLightAction(LightAction lightAction);

    Void visitMotorDriveStopAction(MotorDriveStopAction stopAction);

    Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    Void visitMotorOnAction(MotorOnAction motorOnAction);

    Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    Void visitMotorStopAction(MotorStopAction motorStopAction);

    Void visitPlayFileAction(PlayFileAction playFileAction);

    Void visitPlayNoteAction(PlayNoteAction playNoteAction);

    Void visitPlayRecordingAction(PlayRecordingAction playRecordingAction);

    Void visitRecordStartAction(RecordStartAction recordStartAction);

    Void visitRecordStopAction(RecordStopAction recordStopAction);
    
    Void visitSoundSensor(SoundSensor soundSensor);

    Void visitTapSensor(TapSensor tapSensor);

    Void visitTemperatureSensor(TemperatureSensor temperatureSensor);

    Void visitTimerSensor(TimerSensor timerSensor);

    Void visitTimerReset(TimerReset timerReset);

    Void visitToneAction(ToneAction toneAction);

    Void visitTurnAction(TurnAction turnAction);

    Void visitGetVolumeAction(GetVolumeAction getVolumeAction);

    Void visitSetVolumeAction(SetVolumeAction setVolumeAction);
}
