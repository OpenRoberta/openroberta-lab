package de.fhg.iais.roberta.visitor.arduino;

import de.fhg.iais.roberta.syntax.sensor.arduino.botnroll.VoltageSensor;

public interface BotnrollAstVisitor<V> extends ArduinoAstVisitor<V> {
    V visitVoltageSensor(VoltageSensor<V> voltageSensor);
}
