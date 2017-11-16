package de.fhg.iais.roberta.visitors.arduino;

import de.fhg.iais.roberta.syntax.sensors.arduino.botnroll.VoltageSensor;

public interface BotnrollAstVisitor<V> extends ArduinoAstVisitor<V> {
    V visitVoltageSensor(VoltageSensor<V> voltageSensor);
}
