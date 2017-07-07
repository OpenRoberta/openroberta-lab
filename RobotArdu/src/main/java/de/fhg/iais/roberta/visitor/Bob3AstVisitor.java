package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.bob3.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.bob3.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.botnroll.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;

public interface Bob3AstVisitor<V> {
    /**
     * visit a {@link VoltageSensor}.
     *
     * @param temperatureSensor to be visited
     */
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitTouchSensor(TouchSensor<V> touchSensor);

    V visitLightSensor(LightSensor<V> lightSensor);

    V visitRgbColor(RgbColor<V> rgbColor);

}
