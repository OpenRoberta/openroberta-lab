package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all sensors. There are two ways for a client to find out which kind of a {@link #Sensor}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Sensor extends Phrase {

    public Sensor(Kind kind) {
        super(kind);
    }

}
