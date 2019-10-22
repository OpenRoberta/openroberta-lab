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
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Jaxb2ConfigurationAst {
    public static List<Value> extractValues(Block block) {
        List<Value> values;
        values = block.getValue();
        return values;
    }

    public static List<Field> extractFields(Block block) {
        List<Field> fields;
        fields = block.getField();
        return fields;
    }

    public static String extractOptionalField(List<Field> fields, String name) {
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                return field.getValue();
            }
        }
        return null;
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

    public static ConfigurationAst block2OldConfiguration(Block topBlock, BlocklyDropdownFactory factory, String sensorsPrefix) {
        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        List<Field> fields = extractFields(topBlock);
        addOptionalField(fields, "WHEEL_DIAMETER", wd -> builder.setWheelDiameter(Float.valueOf(wd).floatValue()));
        addOptionalField(fields, "TRACK_WIDTH", wd -> builder.setTrackWidth(Float.valueOf(wd).floatValue()));
        addOptionalField(fields, "IP_ADDRESS", wd -> builder.setIpAddress(wd));
        addOptionalField(fields, "PORT", wd -> builder.setPortNumber(wd));
        addOptionalField(fields, "USERNAME", wd -> builder.setUserName(wd));
        addOptionalField(fields, "PASSWORD", wd -> builder.setPassword(wd));

        List<Value> values = extractValues(topBlock);
        List<ConfigurationComponent> allComponents = extractOldConfigurationComponent(values, factory, sensorsPrefix);

        return builder.addComponents(allComponents).build();
    }

    @FunctionalInterface
    public interface BuilderSetter {
        void add(String val);
    }

    private static void addOptionalField(List<Field> fields, String name, BuilderSetter builderSetter) {
        final String val = extractOptionalField(fields, name);
        if ( val != null ) {
            builderSetter.add(val);
        }
    }

    public static ConfigurationAst block2OldConfigurationWithFixedBase(Block topBlock, BlocklyDropdownFactory factory, String sensorsPrefix) {
        List<Value> values = extractValues(topBlock);
        List<ConfigurationComponent> allComponents = extractOldConfigurationComponent(values, factory, sensorsPrefix);
        return new ConfigurationAst.Builder().setTrackWidth(-1).setWheelDiameter(-1).addComponents(allComponents).build();
    }

    public static List<ConfigurationComponent> extractOldConfigurationComponent(List<Value> values, BlocklyDropdownFactory factory, String sensorsPrefix) {
        List<ConfigurationComponent> allComponents = new ArrayList<>();
        for ( Value value : values ) {
            String portName = value.getName();
            String userDefinedName = portName.substring(1);
            boolean isActor = !portName.startsWith(sensorsPrefix);
            String blocklyName = value.getBlock().getType();
            List<Field> fields = extractFields(value.getBlock());
            Map<String, String> properties = new HashMap<>();
            for ( Field field : fields ) {
                String fKey = field.getName();
                String fValue = field.getValue();
                properties.put(fKey, fValue);
            }
            ConfigurationComponent cc =
                new ConfigurationComponent(factory.getConfigurationComponentTypeByBlocklyName(blocklyName), isActor, portName, userDefinedName, properties);
            allComponents.add(cc);
        }
        return allComponents;
    }

    public static ConfigurationAst blocks2NewConfiguration(List<List<Block>> blocks, BlocklyDropdownFactory factory) {
        List<ConfigurationComponent> allComponents = new ArrayList<>();
        for ( List<Block> block : blocks ) {
            allComponents.add(extractNewConfigurationComponent(block, factory));
        }
        return new ConfigurationAst.Builder().setTrackWidth(0.0f).setWheelDiameter(0.0f).addComponents(allComponents).build();
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
