package de.fhg.iais.roberta.visitor.collect;

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
import de.fhg.iais.roberta.visitor.hardware.IVorwerkVisitor;

/**
 * Collector for the Vorwerk.
 * Adds the blocks missing from the defaults of {@link ICollectorVisitor}.
 * Defines the specific parent implementation to use (the one of the collector) due to unrelated defaults.
 */
public interface IVorwerkCollectorVisitor extends ICollectorVisitor, IVorwerkVisitor<Void> {

    @Override
    default Void visitDropOffSensor(DropOffSensor<Void> dropOffSensor) {
        return null;
    }

    @Override
    default Void visitWallSensor(WallSensor<Void> wallSensor) {
        return null;
    }

    @Override
    default Void visitBrushOn(BrushOn<Void> brushOn) {
        brushOn.getSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitBrushOff(BrushOff<Void> brushOff) {
        return null;
    }

    @Override
    default Void visitSideBrush(SideBrush<Void> sideBrush) {
        return null;
    }

    @Override
    default Void visitVacuumOn(VacuumOn<Void> vacuumOn) {
        vacuumOn.getSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitVacuumOff(VacuumOff<Void> vacuumOff) {
        return null;
    }

    // following methods are used to specify unrelated defaults

    @Override
    default Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return ICollectorVisitor.super.visitPlayNoteAction(playNoteAction);
    }

    @Override
    default Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return ICollectorVisitor.super.visitMotorSetPowerAction(motorSetPowerAction);
    }

    @Override
    default Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return ICollectorVisitor.super.visitVolumeAction(volumeAction);
    }

    @Override
    default Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return ICollectorVisitor.super.visitMotorGetPowerAction(motorGetPowerAction);
    }

    @Override
    default Void visitToneAction(ToneAction<Void> toneAction) {
        return ICollectorVisitor.super.visitToneAction(toneAction);
    }
}
