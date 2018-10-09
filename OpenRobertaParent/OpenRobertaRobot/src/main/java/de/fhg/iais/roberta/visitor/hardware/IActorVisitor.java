package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.control.RelayAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.util.dbc.DbcException;

public interface IActorVisitor<V> extends IHardwareVisitor<V> {

    /**
     * visit a {@link ToneAction}.
     *
     * @param toneAction to be visited
     */
    default V visitToneAction(ToneAction<V> toneAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link PlayNoteAction}.
     *
     * @param playNoteAction
     */
    default V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link VolumeAction}.
     *
     * @param volumeAction to be visited
     */
    default V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link SayTextAction}.
     *
     * @param sayTextAction to be visited
     */
    default V visitSetLanguageAction(SetLanguageAction<V> setLanguageAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link SayTextAction}.
     *
     * @param sayTextAction to be visited
     */
    default V visitSayTextAction(SayTextAction<V> sayTextAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link PlayFileAction}.
     *
     * @param playFileAction
     */
    default V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link DriveAction}.
     *
     * @param driveAction to be visited
     */
    default V visitDriveAction(DriveAction<V> driveAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link CurveAction}.
     *
     * @param turnAction to be visited
     */
    default V visitCurveAction(CurveAction<V> curveAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link TurnAction}.
     *
     * @param turnAction to be visited
     */
    default V visitTurnAction(TurnAction<V> turnAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorGetPowerAction}.
     *
     * @param motorGetPowerAction to be visited
     */
    default V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorOnAction}.
     *
     * @param motorOnAction
     */
    default V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorSetPowerAction}.
     *
     * @param motorSetPowerAction
     */
    default V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorStopAction}.
     *
     * @param motorStopAction
     */
    default V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorDriveStopAction}.
     *
     * @param stopAction
     */
    default V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link LightAction}.
     *
     * @param lightAction to be visited
     */
    default V visitLightAction(LightAction<V> lightAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link LightStatusAction}.
     *
     * @param lightStatusAction to be visited
     */
    default V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link ClearDisplayAction}.
     *
     * @param clearDisplayAction to be visited
     */
    default V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link ShowPictureAction}.
     *
     * @param showPictureAction
     */
    default V visitShowPictureAction(ShowPictureAction<V> showPictureAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link ShowTextAction}.
     *
     * @param showTextAction
     */
    default V visitShowTextAction(ShowTextAction<V> showTextAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link RelayAction}.
     *
     * @param relayAction to be visited
     */
    default V visitRelayAction(RelayAction<V> relayAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link BluetoothRecieveAction}.
     *
     * @param bluetoothReceiveActionbluetoothReceiveAction to be visited
     */
    default V visitBluetoothReceiveAction(BluetoothReceiveAction<V> bluetoothReceiveAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link BluetoothConnectAction}.
     *
     * @param bluetoothConnectAction to be visited
     */
    default V visitBluetoothConnectAction(BluetoothConnectAction<V> bluetoothConnectAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link BluetoothSendAction}.
     *
     * @param bluetoothSendAction to be visited
     */
    default V visitBluetoothSendAction(BluetoothSendAction<V> bluetoothSendAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link BluetoothWaitForConnectionAction}.
     *
     * @param bluetoothWaitForConnection to be visited
     */
    default V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<V> bluetoothWaitForConnection) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link BluetoothCheckConnectAction}.
     *
     * @param bluetoothCheckConnectAction to be visited
     */
    default V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<V> bluetoothCheckConnectAction) {
        throw new DbcException("Not implemented!");
    }

}
