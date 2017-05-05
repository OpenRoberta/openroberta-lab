package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.botnroll.VoltageSensor;

public interface BotnrollAstVisitor<V> extends ArduAstVisitor<V> {
    /**
     * visit a {@link VoltageSensor}.
     *
     * @param voltageSensor to be visited
     */
    V visitVoltageSensor(VoltageSensor<V> voltageSensor);

}
