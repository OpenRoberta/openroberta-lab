package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.arduino.VoltageSensor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface ArduAstVisitor<V> extends AstVisitor<V>, AstSensorsVisitor<V>, AstActorsVisitor<V> {

    /**
     * visit a {@link VoltageSensor}.
     *
     * @param voltageSensor to be visited
     */
    V visitVoltageSensor(VoltageSensor<V> voltageSensor);

}
