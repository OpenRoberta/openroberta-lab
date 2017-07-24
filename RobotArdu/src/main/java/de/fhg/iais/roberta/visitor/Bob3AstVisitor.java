package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.botnroll.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

public interface Bob3AstVisitor<V> extends AstSensorsVisitor<V> {
    /**
     * visit a {@link VoltageSensor}.
     *
     * @param temperatureSensor to be visited
     */
    @Override
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitRgbColor(RgbColor<V> rgbColor);

}
