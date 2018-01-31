package de.fhg.iais.roberta.transformers.arduino;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.Bob3Configuration;
import de.fhg.iais.roberta.factory.IRobotFactory;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2Bob3ConfigurationTransformer {

    public Jaxb2Bob3ConfigurationTransformer(IRobotFactory factory) {
    }

    public Configuration transform(BlockSet blockSet) {
        return new Bob3Configuration();
    }

    public BlockSet transformInverse(Configuration conf) {
        return null;
    }
}
