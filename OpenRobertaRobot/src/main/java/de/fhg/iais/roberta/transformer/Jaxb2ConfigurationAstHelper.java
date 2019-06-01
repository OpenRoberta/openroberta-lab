package de.fhg.iais.roberta.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Jaxb2ConfigurationAstHelper {
    public static List<Value> extractValues(Block block, int numOfValues) {
        List<Value> values;
        values = block.getValue();
        Assert.isTrue(values.size() <= numOfValues, "Values size is not less or equal to " + numOfValues + "!");
        return values;
    }

    public static List<Field> extractFields(Block block, int numOfFields) {
        List<Field> fields;
        fields = block.getField();
        Assert.isTrue(fields.size() <= numOfFields, "Fields size is not less or equal to " + numOfFields + "!");
        return fields;
    }

    public static String extractField(List<Field> fields, String name, int fieldLocation) {
        Field field = fields.get(fieldLocation);
        Assert.isTrue(field.getName().equals(name), "Field name is not equal to " + name + "!");
        return field.getValue();
    }

    public static Block getTopBlock(BlockSet blockSet, String topBlockName) {
        List<Instance> instances = blockSet.getInstance();
        List<Block> blocks;
        Block startingBlock = null;
        for ( Instance instance : instances ) {
            blocks = instance.getBlock();
            for ( Block block : blocks ) {
                if ( block.getType().equals(topBlockName) ) {
                    startingBlock = block;
                }
            }
        }
        if ( startingBlock == null ) {
            throw new DbcException("No valid base/starting block was found in the configuration");
        }
        return startingBlock;
    }

    public static Configuration block2OldConfiguration(Block topBlock, BlocklyDropdownFactory factory, String sensorsPrefix) {
        List<Field> fields = extractFields(topBlock, (short) 2);
        float wheelDiameter = Float.valueOf(extractField(fields, "WHEEL_DIAMETER", (short) 0)).floatValue();
        float trackWidth = Float.valueOf(extractField(fields, "TRACK_WIDTH", (short) 1)).floatValue();
        //TODO: should 8 here be a parameter of this method?
        List<Value> values = extractValues(topBlock, (short) 8);
        List<ConfigurationComponent> allComponents = extractOldConfigurationComponent(values, factory, sensorsPrefix);

        return new Configuration.Builder().setTrackWidth(trackWidth).setWheelDiameter(wheelDiameter).addComponents(allComponents).build();
    }

    public static Configuration block2OldConfigurationWithFixedBase(
        Block topBlock,
        BlocklyDropdownFactory factory,
        String sensorsPrefix,
        int configurationBlockQuantity) {
        List<Value> values = extractValues(topBlock, (short) configurationBlockQuantity);
        List<ConfigurationComponent> allComponents = extractOldConfigurationComponent(values, factory, sensorsPrefix);
        return new Configuration.Builder().setTrackWidth(-1).setWheelDiameter(-1).addComponents(allComponents).build();
    }

    public static List<ConfigurationComponent> extractOldConfigurationComponent(List<Value> values, BlocklyDropdownFactory factory, String sensorsPrefix) {
        List<ConfigurationComponent> allComponents = new ArrayList<>();
        for ( Value value : values ) {
            String portName = value.getName();
            String userDefinedName = portName.substring(1);
            boolean isActor = !portName.startsWith(sensorsPrefix);
            String blocklyName = value.getBlock().getType();
            List<Field> fields = extractFields(value.getBlock(), (short) 3);
            Map<String, String> properties = new HashMap<>();
            for ( Field field : fields ) {
                String fKey = field.getName();
                String fValue = field.getValue();
                properties.put(fKey, fValue);
            }
            ConfigurationComponent cc =
                new ConfigurationComponent(
                    factory.getConfigurationComponentTypeByBlocklyName(blocklyName),
                    isActor,
                    portName,
                    userDefinedName,
                    properties);
            allComponents.add(cc);
        }
        return allComponents;
    }

    public static Configuration blocks2NewConfiguration(List<List<Block>> blocks, BlocklyDropdownFactory factory) {
        List<ConfigurationComponent> allComponents = new ArrayList<>();
        for ( List<Block> block : blocks ) {
            allComponents.add(extractNewConfigurationComponent(block, factory));
        }
        return new Configuration.Builder().setTrackWidth(0.0f).setWheelDiameter(0.0f).addComponents(allComponents).build();
    }

    private static ConfigurationComponent extractNewConfigurationComponent(List<Block> block, BlocklyDropdownFactory factory) {
        Block firstBlock = block.get(0);
        String componentType = factory.getConfigurationComponentTypeByBlocklyName(firstBlock.getType());
        String userDefinedName = firstBlock.getField().get(0).getValue();
        Map<String, String> m = new HashMap<>();
        for ( int i = 1; i < firstBlock.getField().size(); i++ ) {
            m.put(firstBlock.getField().get(i).getName(), firstBlock.getField().get(i).getValue());
        }
        return new ConfigurationComponent(componentType, true, null, userDefinedName, m);
    }

}
