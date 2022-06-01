package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;

public class Sensor extends Phrase<Void> {
    public Sensor() {
        super(null, BlocklyBlockProperties.make("SENSOR", "sensor"), null);
    }
}
