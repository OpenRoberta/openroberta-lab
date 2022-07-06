package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;

public interface IFestobionicflowerVisitor<V> {

    V visitLedOffAction(LedOffAction ledOffAction);

    V visitLedOnAction(LedOnAction ledOnAction);

    V visitStepMotorAction(StepMotorAction stepMotorAction);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitLightSensor(LightSensor lightSensor);

}
