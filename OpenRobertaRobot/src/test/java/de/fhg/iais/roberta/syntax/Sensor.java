package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;

@NepoBasic(containerType = "SENSOR", category = "SENSOR", blocklyNames = {"sensor"})
public class Sensor extends Phrase<Void> {
    public Sensor() {
        super(BlocklyBlockProperties.make("SENSOR", "1"), null);
    }
}
