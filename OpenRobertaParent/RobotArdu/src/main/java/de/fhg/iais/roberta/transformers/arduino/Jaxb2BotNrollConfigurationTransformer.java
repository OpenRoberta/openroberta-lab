package de.fhg.iais.roberta.transformers.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAstHelper;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2BotNrollConfigurationTransformer {
    BlocklyDropdownFactory factory;

    public Jaxb2BotNrollConfigurationTransformer(BlocklyDropdownFactory factory) {
        this.factory = factory;
    }

    public Configuration transform(BlockSet blockSet) {
        List<Instance> instances = blockSet.getInstance();
        List<Block> blocks = instances.get(0).getBlock();
        return Jaxb2ConfigurationAstHelper.block2OldConfigurationWithFixedBase(blocks.get(0), this.factory, "S", 14);
    }
}
