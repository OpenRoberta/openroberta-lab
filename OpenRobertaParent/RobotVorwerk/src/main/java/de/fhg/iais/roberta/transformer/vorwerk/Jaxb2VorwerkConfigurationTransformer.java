package de.fhg.iais.roberta.transformer.vorwerk;

import static de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAstHelper.extractField;
import static de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAstHelper.extractFields;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.vorwerk.VorwerkConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2VorwerkConfigurationTransformer {
    IRobotFactory factory;

    public Jaxb2VorwerkConfigurationTransformer(IRobotFactory factory) {
        this.factory = factory;
    }

    public Configuration transform(BlockSet blockSet) {
        List<Instance> instances = blockSet.getInstance();
        List<Block> blocks = instances.get(0).getBlock();
        return blockToBrickConfiguration(blocks.get(0));
    }

    private Configuration blockToBrickConfiguration(Block block) {
        switch ( block.getType() ) {
            case "robBrick_vorwerk-Brick":
                List<Field> fields = extractFields(block, (short) 4);
                String ipAddress = extractField(fields, "IP_ADDRESS", 0);
                String portNumber = extractField(fields, "PORT", 1);
                String userName = extractField(fields, "USERNAME", 2);
                String password = extractField(fields, "PASSWORD", 3);

                return new VorwerkConfiguration.Builder().setIpAddres(ipAddress).setPassword(password).setPortNumber(portNumber).setUserName(userName).build();
            default:
                throw new DbcException("There was no correct configuration block found!");
        }
    }
}
