package de.fhg.iais.roberta.transformer.nxt;

import static de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAstHelper.block2OldConfiguration;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAstHelper;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2NxtConfigurationTransformer {
    BlocklyDropdownFactory factory;

    public Jaxb2NxtConfigurationTransformer(IRobotFactory factory) {
        this.factory = factory.getBlocklyDropdownFactory();
    }

    public Configuration transform(BlockSet blockSet) {
        Block startingBlock = Jaxb2ConfigurationAstHelper.getTopBlock(blockSet, "robBrick_EV3-Brick");
        return block2OldConfiguration(startingBlock, this.factory, "S");
    }

}
