package de.fhg.iais.roberta.transformers.arduino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.ConfigurationBlockType;
import de.fhg.iais.roberta.components.arduino.ArduinoConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2ArduinoConfigurationTransformer {
    IRobotFactory factory;

    public Jaxb2ArduinoConfigurationTransformer(IRobotFactory factory) {
        this.factory = factory;
    }

    public Configuration transform(BlockSet blockSet) {
        List<Instance> instances = blockSet.getInstance();
        List<List<Block>> blocks = new ArrayList<>();
        for ( int i = 0; i < instances.size(); i++ ) {
            blocks.add(instances.get(i).getBlock());
        }
        return blockToBrickConfiguration(blocks);
    }

    public BlockSet transformInverse(Configuration conf) {
        int idCount = 1;
        BlockSet blockSet = new BlockSet();
        Instance instance = new Instance();
        blockSet.getInstance().add(instance);
        instance.setX("20");
        instance.setY("20");
        Block block = mkBlock(idCount++);
        block.setType("robBrick_Arduino-board");
        //TODO: add other configuration blocks and fix the whole reverse transform for the Arduino
        return blockSet;
    }

    private Block mkBlock(int id) {
        Block block = new Block();
        block.setId("" + id);
        block.setInline(false);
        block.setDisabled(false);
        block.setIntask(true);
        return block;
    }

    private Configuration blockToBrickConfiguration(List<List<Block>> blocks) {
        Map<String, ConfigurationBlock> configurationBlocks = new HashMap<String, ConfigurationBlock>();
        for ( int i = 1; i < blocks.size(); i++ ) {
            configurationBlocks.put(blocks.get(i).get(0).getField().get(0).getValue(), extractConfigurationBlockComponents(blocks.get(i)));
        }
        return new ArduinoConfiguration(configurationBlocks);
    }

    private ConfigurationBlock extractConfigurationBlockComponents(List<Block> block) {
        ConfigurationBlockType confType = ConfigurationBlockType.get(block.get(0).getType());
        String name = block.get(0).getField().get(0).getValue();
        Map<String, String> confPorts = new HashMap<String, String>();
        for ( int i = 1; i < block.get(0).getField().size(); i++ ) {
            confPorts.put(block.get(0).getField().get(i).getName(), block.get(0).getField().get(i).getValue());
        }
        return new ConfigurationBlock(confType, name, confPorts);
    }
}
