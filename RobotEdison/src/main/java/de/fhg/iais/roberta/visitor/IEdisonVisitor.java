package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;

public interface IEdisonVisitor<V> extends ILightVisitor<V>, IDifferentialMotorVisitor<V>, ISoundVisitor<V> {
    V visitSendIRAction(SendIRAction<V> sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction);

    V visitResetSensor(ResetSensor<V> resetSensor);

    V visitInfraredSensor(InfraredSensor<V> infraredSensor);

    V visitIRSeekerSensor(IRSeekerSensor<V> irSeekerSensor);

    V visitLightSensor(LightSensor<V> lightSensor);

    V visitSoundSensor(SoundSensor<V> soundSensor);

    V visitKeysSensor(KeysSensor<V> keysSensor);

    V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample);
}
