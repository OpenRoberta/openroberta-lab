package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoBasic(name = "OTHER_SENSOR", category = "SENSOR", blocklyNames = {"other_sensor"})
public final class OtherSensor extends Phrase {
    public OtherSensor() {
        super(BlocklyProperties.make("OTHER_SENSOR", "1"));
    }
}
