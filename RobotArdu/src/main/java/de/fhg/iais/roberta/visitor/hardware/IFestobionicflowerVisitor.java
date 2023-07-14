package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface IFestobionicflowerVisitor<V> extends IVisitor<V> {
    V visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction);

    V visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction);

    V visitStepMotorAction(StepMotorAction stepMotorAction);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitLightSensor(LightSensor lightSensor);
}
