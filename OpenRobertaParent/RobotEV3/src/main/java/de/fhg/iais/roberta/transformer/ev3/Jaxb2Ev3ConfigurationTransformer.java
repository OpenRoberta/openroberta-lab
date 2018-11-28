package de.fhg.iais.roberta.transformer.ev3;

import static de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAstHelper.block2OldConfiguration;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAstHelper;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2Ev3ConfigurationTransformer {
    BlocklyDropdownFactory factory;

    public Jaxb2Ev3ConfigurationTransformer(BlocklyDropdownFactory factory) {
        this.factory = factory;
    }

    public Configuration transform(BlockSet blockSet) {
        Block startingBlock = Jaxb2ConfigurationAstHelper.getTopBlock(blockSet, "robBrick_EV3-Brick");
        return block2OldConfiguration(startingBlock, this.factory, "S");
    }

}
