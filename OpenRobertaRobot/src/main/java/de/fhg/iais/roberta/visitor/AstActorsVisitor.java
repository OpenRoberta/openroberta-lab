package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.generic.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.CurveAction;
import de.fhg.iais.roberta.syntax.action.generic.DriveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightAction;
import de.fhg.iais.roberta.syntax.action.generic.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.generic.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.ToneAction;
import de.fhg.iais.roberta.syntax.action.generic.TurnAction;
import de.fhg.iais.roberta.syntax.action.generic.VolumeAction;
import de.fhg.iais.roberta.syntax.action.generic.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.communication.BluetoothWaitForConnectionAction;

public interface AstActorsVisitor<V> extends AstVisitor<V> {
    /**
     * visit a {@link DriveAction}.
     *
     * @param driveAction to be visited
     */
    V visitDriveAction(DriveAction<V> driveAction);

    /**
     * visit a {@link CurveAction}.
     *
     * @param turnAction to be visited
     */
    V visitCurveAction(CurveAction<V> curveAction);

    /**
     * visit a {@link TurnAction}.
     *
     * @param turnAction to be visited
     */
    V visitTurnAction(TurnAction<V> turnAction);

    /**
     * visit a {@link LightAction}.
     *
     * @param lightAction to be visited
     */
    V visitLightAction(LightAction<V> lightAction);

    /**
     * visit a {@link LightStatusAction}.
     *
     * @param lightStatusAction to be visited
     */
    V visitLightStatusAction(LightStatusAction<V> lightStatusAction);

    /**
     * visit a {@link MotorGetPowerAction}.
     *
     * @param motorGetPowerAction to be visited
     */
    V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction);

    /**
     * visit a {@link MotorOnAction}.
     *
     * @param motorOnAction
     */
    V visitMotorOnAction(MotorOnAction<V> motorOnAction);

    /**
     * visit a {@link MotorSetPowerAction}.
     *
     * @param motorSetPowerAction
     */
    V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction);

    /**
     * visit a {@link MotorStopAction}.
     *
     * @param motorStopAction
     */
    V visitMotorStopAction(MotorStopAction<V> motorStopAction);

    /**
     * visit a {@link ClearDisplayAction}.
     *
     * @param clearDisplayAction to be visited
     */
    V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction);

    /**
     * visit a {@link VolumeAction}.
     *
     * @param volumeAction to be visited
     */
    V visitVolumeAction(VolumeAction<V> volumeAction);

    /**
     * visit a {@link PlayFileAction}.
     *
     * @param playFileAction
     */
    V visitPlayFileAction(PlayFileAction<V> playFileAction);

    /**
     * visit a {@link ShowPictureAction}.
     *
     * @param showPictureAction
     */
    V visitShowPictureAction(ShowPictureAction<V> showPictureAction);

    /**
     * visit a {@link ShowTextAction}.
     *
     * @param showTextAction
     */
    V visitShowTextAction(ShowTextAction<V> showTextAction);

    /**
     * visit a {@link MotorDriveStopAction}.
     *
     * @param stopAction
     */
    V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction);

    /**
     * visit a {@link ToneAction}.
     *
     * @param toneAction to be visited
     */
    V visitToneAction(ToneAction<V> toneAction);

    /**
     * visit a {@link BluetoothRecieveAction}.
     *
     * @param bluetoothReceiveActionbluetoothReceiveAction to be visited
     */
    V visitBluetoothReceiveAction(BluetoothReceiveAction<V> bluetoothReceiveAction);

    /**
     * visit a {@link BluetoothConnectAction}.
     *
     * @param bluetoothConnectAction to be visited
     */
    V visitBluetoothConnectAction(BluetoothConnectAction<V> bluetoothConnectAction);

    /**
     * visit a {@link BluetoothSendAction}.
     *
     * @param bluetoothSendAction to be visited
     */
    V visitBluetoothSendAction(BluetoothSendAction<V> bluetoothSendAction);

    /**
     * visit a {@link BluetoothWaitForConnectionAction}.
     *
     * @param bluetoothWaitForConnection to be visited
     */
    V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<V> bluetoothWaitForConnection);

    /**
     * visit a {@link BluetoothCheckConnectAction}.
     *
     * @param bluetoothCheckConnectAction to be visited
     */
    V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<V> bluetoothCheckConnectAction);
}
