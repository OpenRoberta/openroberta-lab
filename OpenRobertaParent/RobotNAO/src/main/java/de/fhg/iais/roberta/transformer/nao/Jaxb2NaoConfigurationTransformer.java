package de.fhg.iais.roberta.transformer.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.nao.NAOConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2NaoConfigurationTransformer {
    IRobotFactory factory;

    public Jaxb2NaoConfigurationTransformer(IRobotFactory factory) {
        this.factory = factory;
    }

    public Configuration transform(BlockSet blockSet) {
        List<Instance> instances = blockSet.getInstance();
        List<Block> blocks = instances.get(0).getBlock();
        return blockToBrickConfiguration(blocks.get(0));
    }

    public BlockSet transformInverse(NAOConfiguration conf) {
        int idCount = 1;
        BlockSet blockSet = new BlockSet();
        Instance instance = new Instance();
        blockSet.getInstance().add(instance);
        instance.setX("20");
        instance.setY("20");
        Block block = mkBlock(idCount); // Replace by idCount++ if more are needed to get unique ids
        block.setType("naoBrick_NAO-Brick");
        instance.getBlock().add(block);
        List<Field> fields = block.getField();
        fields.add(mkField("IP_ADDRESS", conf.getIpAddress()));
        fields.add(mkField("PORT", conf.getPortNumber()));
        fields.add(mkField("USERNAME", conf.getUserName()));
        fields.add(mkField("PASSWORD", conf.getPassword()));
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

    private Configuration blockToBrickConfiguration(Block block) {
        switch ( block.getType() ) {
            case "naoBrick_NAO-Brick":
                List<Field> fields = extractFields(block, (short) 4);
                String ipAddress = extractField(fields, "IP_ADDRESS", 0);
                String portNumber = extractField(fields, "PORT", 1);
                String userName = extractField(fields, "USERNAME", 2);
                String password = extractField(fields, "PASSWORD", 3);

                return new NAOConfiguration.Builder().setIpAddres(ipAddress).setPassword(password).setPortNumber(portNumber).setUserName(userName).build();
            default:
                throw new DbcException("There was no correct configuration block found!");
        }
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
