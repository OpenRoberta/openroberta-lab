package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.bob3.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.botnroll.VoltageSensor;

public interface Bob3AstVisitor<V> extends ArduAstVisitor<V> {
    /**
     * visit a {@link VoltageSensor}.
     *
     * @param temperatureSensor to be visited
     */
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

}
