package de.fhg.iais.roberta.transformer.wedo;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.ConfigurationBlockType;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.Quadruplet;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2WeDoConfigurationTransformer {
    IRobotFactory factory;

    public Jaxb2WeDoConfigurationTransformer(IRobotFactory factory) {
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
        block.setType("robBrick_WeDo-board");
        //TODO: add other configuration blocks and fix the whole reverse transform for the WeDo
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

    private Configuration blockToBrickConfiguration(List<List<Block>> blocks) {
        boolean confBrickFound = false;
        // Quadruplet: block type; block name; list of block' port names; list of block's pin names
        List<Quadruplet<ConfigurationBlock, String, List<String>, List<String>>> configurationBlocks = new ArrayList<>();

        // List of blocks is unsorted so maybe the "brick" block is not the first!
        for ( int i = 0; i < blocks.size(); i++ ) {
            if ( blocks.get(i).get(0).getType().equals("robBrick_WeDo-Brick") ) {
                confBrickFound = true;
                continue;
            } else {
                configurationBlocks.add(extractConfigurationBlockComponents(blocks.get(i)));
            }
        }
        if ( !confBrickFound ) {
            throw new DbcException("There was no correct configuration block found! ");
        }
        return new WeDoConfiguration(configurationBlocks).getConfiguration();
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

    private List<Field> extractFields(Block block, int numOfFields) {
        List<Field> fields;
        fields = block.getField();
        Assert.isTrue(fields.size() == numOfFields, "Number of fields is not equal to " + numOfFields + "!");
        return fields;
    }

    private String extractField(List<Field> fields, String name, int fieldLocation) {
        Field field = fields.get(fieldLocation);
        Assert.isTrue(field.getName().equals(name), "Field name is not equal to " + name + "!");
        return field.getValue();
    }
}
