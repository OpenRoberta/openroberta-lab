package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
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
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;

public interface IEdisonVisitor<V> extends IVisitor<V> {
    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitSendIRAction(SendIRAction sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction receiveIRAction);

    V visitResetSensor(ResetSensor resetSensor);

    V visitDriveAction(DriveAction driveAction);

    V visitCurveAction(CurveAction curveAction);

    V visitTurnAction(TurnAction turnAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);

    V visitLightAction(LightAction lightAction);

    V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitToneAction(ToneAction toneAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitGetVolumeAction(GetVolumeAction getVolumeAction);

    V visitSetVolumeAction(SetVolumeAction setVolumeAction);

    V visitPlayFileAction(PlayFileAction playFileAction);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitIRSeekerSensor(IRSeekerSensor irSeekerSensor);

    V visitLightSensor(LightSensor lightSensor);

    V visitLightStatusAction(LightStatusAction lightStatusAction);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitWaitStmt(WaitStmt waitStmt);

    V visitGetSampleSensor(GetSampleSensor sensorGetSample);
}
