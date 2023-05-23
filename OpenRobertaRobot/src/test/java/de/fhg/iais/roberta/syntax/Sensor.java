package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoBasic(name = "SENSOR", category = "CONFIGURATION_SENSOR", blocklyNames = {"sensor"})
public class Sensor extends Phrase {
    public Sensor() {
        super(BlocklyProperties.make("SENSOR", "1"));
    }
}
