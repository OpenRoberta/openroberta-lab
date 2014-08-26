package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all sensors. There are two ways for a client to find out which kind of a {@link #Sensor}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Sensor<V> extends Phrase<V> {

    /**
     * This constructor set the kind of the sensor object used in the AST (abstract syntax tree). All possible kinds can be found in {@link Kind}.
     * 
     * @param kind of the the sensor object used in AST
     */
    public Sensor(Kind kind) {
        super(kind);
    }

}
