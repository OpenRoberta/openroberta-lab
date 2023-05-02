package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
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
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;

public interface INxtVisitor<V> extends IVisitor<V> {

    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction);

    V visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction);

    V visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction);

    V visitColorSensor(ColorSensor colorSensor);

    V visitConnectConst(ConnectConst connectConst);

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitEncoderReset(EncoderReset encoderReset);

    V visitHTColorSensor(HTColorSensor htColorSensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitDriveAction(DriveAction driveAction);

    V visitTurnAction(TurnAction turnAction);

    V visitCurveAction(CurveAction curveAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);

    V visitLightAction(LightAction lightAction);

    V visitLightSensor(LightSensor lightSensor);

    V visitMathOnListFunct(MathOnListFunct mathOnListFunct);

    V visitMathSingleFunct(MathSingleFunct mathSingleFunct);

    V visitPlayFileAction(PlayFileAction playFileAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitShowTextAction(ShowTextAction showTextAction);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitToneAction(ToneAction toneAction);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitGetVolumeAction(GetVolumeAction getVolumeAction);

    V visitSetVolumeAction(SetVolumeAction setVolumeAction);
}
