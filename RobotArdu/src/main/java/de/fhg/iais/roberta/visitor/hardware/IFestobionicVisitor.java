package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.visitor.IVisitor;


public interface IFestobionicVisitor<V> extends IVisitor<V> {
    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitLightAction(LightAction lightAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);
}
