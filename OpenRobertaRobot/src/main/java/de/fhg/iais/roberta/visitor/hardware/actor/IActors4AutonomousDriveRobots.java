package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.util.dbc.DbcException;

public interface IActors4AutonomousDriveRobots<V> extends IDifferentialMotorVisitor<V>, IDisplayVisitor<V>, ILightVisitor<V>, ISoundVisitor<V> {

    /**
     * visit a {@link ToneAction}.
     *
     * @param toneAction to be visited
     */
    @Override
    default V visitToneAction(ToneAction<V> toneAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link PlayNoteAction}.
     *
     * @param playNoteAction
     */
    @Override
    default V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link VolumeAction}.
     *
     * @param volumeAction to be visited
     */
    @Override
    default V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link PlayFileAction}.
     *
     * @param playFileAction
     */
    @Override
    default V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link DriveAction}.
     *
     * @param driveAction to be visited
     */
    @Override
    default V visitDriveAction(DriveAction<V> driveAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link CurveAction}.
     *
     * @param turnAction to be visited
     */
    @Override
    default V visitCurveAction(CurveAction<V> curveAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link TurnAction}.
     *
     * @param turnAction to be visited
     */
    @Override
    default V visitTurnAction(TurnAction<V> turnAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorGetPowerAction}.
     *
     * @param motorGetPowerAction to be visited
     */
    @Override
    default V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorOnAction}.
     *
     * @param motorOnAction
     */
    @Override
    default V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorSetPowerAction}.
     *
     * @param motorSetPowerAction
     */
    @Override
    default V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorStopAction}.
     *
     * @param motorStopAction
     */
    @Override
    default V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link MotorDriveStopAction}.
     *
     * @param stopAction
     */
    @Override
    default V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link LightAction}.
     *
     * @param lightAction to be visited
     */
    @Override
    default V visitLightAction(LightAction<V> lightAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link LightStatusAction}.
     *
     * @param lightStatusAction to be visited
     */
    @Override
    default V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link ClearDisplayAction}.
     *
     * @param clearDisplayAction to be visited
     */
    @Override
    default V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        throw new DbcException("Not implemented!");
    }

    /**
     * visit a {@link ShowTextAction}.
     *
     * @param showTextAction
     */
    @Override
    default V visitShowTextAction(ShowTextAction<V> showTextAction) {
        throw new DbcException("Not implemented!");
    }

}
