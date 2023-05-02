package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
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
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextWithSpeedAndPitchAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassCalibrate;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;

public interface IEv3Visitor<V> extends IVisitor<V> {
    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitLightAction(LightAction lightAction);

    V visitLightStatusAction(LightStatusAction lightStatusAction);

    V visitPlayFileAction(PlayFileAction playFileAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitSayTextAction(SayTextAction sayTextAction);

    V visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction);

    V visitSetLanguageAction(SetLanguageAction setLanguageAction);

    V visitShowPictureAction(ShowPictureAction showPictureAction);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction);

    V visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction);

    V visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction);

    V visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction);

    V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection);

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitColorSensor(ColorSensor colorSensor);

    V visitShowTextAction(ShowTextAction showTextAction);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitEncoderReset(EncoderReset encoderReset);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitGyroReset(GyroReset gyroReset);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitToneAction(ToneAction toneAction);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitCompassSensor(CompassSensor compassSensor);

    V visitCompassCalibrate(CompassCalibrate compassCalibrate);

    V visitIRSeekerSensor(IRSeekerSensor irSeekerSensor);

    V visitHTColorSensor(HTColorSensor htColorSensor);

    V visitGetVolumeAction(GetVolumeAction getVolumeAction);

    V visitSetVolumeAction(SetVolumeAction setVolumeAction);

    V visitCurveAction(CurveAction curveAction);

    V visitTurnAction(TurnAction turnAction);

    Void visitMotorOnAction(MotorOnAction motorOnAction);

    Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    Void visitMotorStopAction(MotorStopAction motorStopAction);

    V visitDriveAction(DriveAction driveAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);
}
