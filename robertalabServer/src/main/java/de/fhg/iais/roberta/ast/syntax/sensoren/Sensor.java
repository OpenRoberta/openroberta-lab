package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all sensor blocks.
 */
public abstract class Sensor extends Phrase {

    public Sensor(Kind kind) {
        super(kind);
    }

}
