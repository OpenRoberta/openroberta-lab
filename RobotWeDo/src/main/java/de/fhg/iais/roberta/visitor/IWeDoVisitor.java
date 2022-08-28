package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IWeDoVisitor<V> extends IMotorVisitor<V>, ISoundVisitor<V>, ILightVisitor<V>, IDisplayVisitor<V>, ISensorVisitor<V> {

    @Override
    default V visitVolumeAction(VolumeAction volumeAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    default V visitPlayFileAction(PlayFileAction playFileAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    default V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    default V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        throw new DbcException("operation not supported");
    }

}
