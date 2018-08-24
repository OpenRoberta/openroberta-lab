package de.fhg.iais.roberta.transformers.arduino;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.ConfigurationBlockType;
import de.fhg.iais.roberta.components.arduino.ArduinoConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.Quadruplet;

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

    private Field mkField(String name, String value) {
        Field field = new Field();
        field.setName(name);
        field.setValue(value);
        return field;
    }

    @SuppressWarnings("rawtypes")
    private Configuration blockToBrickConfiguration(List<List<Block>> blocks) {
        // Quadruplet: block type; block name; list of block' port names; list of block's pin names
        List<Quadruplet<ConfigurationBlock, String, List<String>, List<String>>> configurationBlocks = new ArrayList<>();
        switch ( blocks.get(0).get(0).getType() ) {
            case "robBrick_Arduino-Brick":
                for ( int i = 1; i < blocks.size(); i++ ) {
                    configurationBlocks.add(extractConfigurationBlockComponents(blocks.get(i)));
                }
                // TODO pass the board type in a more sensible way
                return new ArduinoConfiguration(configurationBlocks);
            default:
                for ( int i = 0; i < blocks.size(); i++ ) {
                    configurationBlocks.add(extractConfigurationBlockComponents(blocks.get(i)));
                }
                return new ArduinoConfiguration(configurationBlocks);
            //throw new DbcException("There was no correct configuration block found! " + blocks.get(0).get(0).getType());
        }
    }

    private Quadruplet<ConfigurationBlock, String, List<String>, List<String>> extractConfigurationBlockComponents(List<Block> block) {
        ConfigurationBlock confBlock = new ConfigurationBlock(ConfigurationBlockType.get(block.get(0).getType()));
        String name = block.get(0).getField().get(0).getValue();
        List<String> portNames = new ArrayList<>();
        List<String> pinNumbers = new ArrayList<>();
        for ( int i = 1; i < block.get(0).getField().size(); i++ ) {
            portNames.add(block.get(0).getField().get(i).getName());
            pinNumbers.add(block.get(0).getField().get(i).getValue());
        }
        return Quadruplet.of(confBlock, name, portNames, pinNumbers);
    }
}
