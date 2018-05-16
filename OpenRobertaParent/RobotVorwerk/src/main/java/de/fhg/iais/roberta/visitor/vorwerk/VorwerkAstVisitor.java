package de.fhg.iais.roberta.visitor.vorwerk;

import de.fhg.iais.roberta.syntax.sensor.vorwerk.DropOffSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.WallSensor;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface VorwerkAstVisitor<V> extends AstVisitor<V>, AstSensorsVisitor<V> {

    public V visitDropOffSensor(DropOffSensor<V> dropOffSensor);

    public V visitWallSensor(WallSensor<V> wallSensor);

}