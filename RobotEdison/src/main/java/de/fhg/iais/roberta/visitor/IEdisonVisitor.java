package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.actor.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actor.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.sensor.edison.ResetSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;

public interface IEdisonVisitor<V> extends ILightVisitor<V>, IDifferentialMotorVisitor<V>, ISoundVisitor<V> {
    V visitSendIRAction(SendIRAction sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction receiveIRAction);

    V visitResetSensor(ResetSensor resetSensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitIRSeekerSensor(IRSeekerSensor irSeekerSensor);

    V visitLightSensor(LightSensor lightSensor);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitGetSampleSensor(GetSampleSensor sensorGetSample);
}
