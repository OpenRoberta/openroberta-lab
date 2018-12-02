package de.fhg.iais.roberta.transformers.arduino;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAstHelper;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2MbotConfigurationTransformer {
    BlocklyDropdownFactory factory;

    public Jaxb2MbotConfigurationTransformer(BlocklyDropdownFactory factory) {
        this.factory = factory;
    }

    public Configuration transform(BlockSet blockSet) {
        Block startingBlock = Jaxb2ConfigurationAstHelper.getTopBlock(blockSet, "robBrick_mBot-Brick");
        return Jaxb2ConfigurationAstHelper.block2OldConfigurationWithFixedBase(startingBlock, this.factory, "P", 6);
    }
}
