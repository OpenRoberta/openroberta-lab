package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actor.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorStopAction;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISimpleSoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IBotnrollVisitor<V> extends IDifferentialMotorVisitor<V>, IDisplayVisitor<V>, ILightVisitor<V>, ISensorVisitor<V>, ISimpleSoundVisitor<V> {

    @Override
    default V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorStopAction(MotorStopAction motorStopAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitLightStatusAction(LightStatusAction lightStatusAction) {
        throw new DbcException("Not supported!");
    }
}
