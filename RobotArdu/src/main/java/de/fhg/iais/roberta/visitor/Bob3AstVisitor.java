package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.bob3.Bob3LightSensor;
import de.fhg.iais.roberta.syntax.sensor.bob3.Bob3TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.bob3.Bob3TouchSensor;

public interface Bob3AstVisitor<V> extends ArduAstVisitor<V> {

    V visitBob3TemperatureSensor(Bob3TemperatureSensor<V> temperatureSensor);

    V visitRgbColor(RgbColor<V> rgbColor);

    V visitTouchSensor(Bob3TouchSensor<V> touchSensor);

    V visitLightSensor(Bob3LightSensor<V> lightSensor);

    V visitBodyLEDAction(BodyLEDAction<V> bodyLEDAction);

}
