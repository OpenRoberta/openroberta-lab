package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actor.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actor.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actor.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;

public interface IFestobionicflowerVisitor<V> extends ILightVisitor<V> {

    V visitLedOffAction(LedOffAction ledOffAction);

    V visitLedOnAction(LedOnAction ledOnAction);

    V visitStepMotorAction(StepMotorAction stepMotorAction);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitLightSensor(LightSensor lightSensor);

    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }
}
