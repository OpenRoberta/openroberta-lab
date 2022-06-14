package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;

@NepoBasic(containerType = "OTHER_SENSOR", category = "SENSOR", blocklyNames = {"other_sensor"})
public class OtherSensor extends Phrase<Void> {
    public OtherSensor() {
        super(BlocklyBlockProperties.make("OTHER_SENSOR", "1"), null);
    }
}
