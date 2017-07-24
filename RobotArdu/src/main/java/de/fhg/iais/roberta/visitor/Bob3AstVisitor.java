package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.bob3.Bob3LightSensor;
import de.fhg.iais.roberta.syntax.sensor.bob3.Bob3TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.botnroll.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;

public interface Bob3AstVisitor<V> extends ArduAstVisitor<V> {
    /**
     * visit a {@link VoltageSensor}.
     *
     * @param temperatureSensor to be visited
     */
    @Override
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitRgbColor(RgbColor<V> rgbColor);

    V visitTouchSensor(Bob3TouchSensor<V> touchSensor);

    V visitLightSensor(Bob3LightSensor<V> lightSensor);

}
