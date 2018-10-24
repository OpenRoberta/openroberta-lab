package de.fhg.iais.roberta.transformer.ev3;

import static de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAstHelper.block2OldConfiguration;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2Ev3ConfigurationTransformer {
    BlocklyDropdownFactory factory;

    public Jaxb2Ev3ConfigurationTransformer(BlocklyDropdownFactory factory) {
        this.factory = factory;
    }

    public Configuration transform(BlockSet blockSet) {
        List<Instance> instances = blockSet.getInstance();
        List<Block> blocks = instances.get(0).getBlock();
        return block2OldConfiguration(blocks.get(0), "robBrick_EV3-Brick", this.factory, "S");
    }

}
