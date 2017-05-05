package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.botnroll.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.makeblock.TemperatureSensor;

public interface MakeblockAstVisitor<V> extends ArduAstVisitor<V> {
    /**
     * visit a {@link VoltageSensor}.
     *
     * @param temperatureSensor to be visited
     */
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

}
