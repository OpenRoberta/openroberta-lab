package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOn;
import de.fhg.iais.roberta.syntax.action.vorwerk.SideBrush;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOn;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.DropOffSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.WallSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IVorwerkVisitor<V> extends ISensorVisitor<V>, ISoundVisitor<V>, IDifferentialMotorVisitor<V> {

    public V visitDropOffSensor(DropOffSensor<V> dropOffSensor);

    public V visitWallSensor(WallSensor<V> wallSensor);

    public V visitBrushOn(BrushOn<V> brushOn);

    public V visitBrushOff(BrushOff<V> brushOff);

    public V visitSideBrush(SideBrush<V> sideBrush);

    public V visitVacuumOn(VacuumOn<V> vacuumOn);

    public V visitVacuumOff(VacuumOff<V> vacuumOff);

    @Override
    default V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitToneAction(ToneAction<V> toneAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("Not supported!");
    }

}