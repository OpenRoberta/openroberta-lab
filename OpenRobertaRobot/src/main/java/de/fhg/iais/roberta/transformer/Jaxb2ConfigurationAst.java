package de.fhg.iais.roberta.transformer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.ConfigurationComponentLeaf;
import de.fhg.iais.roberta.components.ConfigurationComponentNode;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.Callback;
import de.fhg.iais.roberta.util.dbc.DbcException;

public final class Jaxb2ConfigurationAst {

    private static final Pattern BRICK_BLOCK_PATTERN = Pattern.compile("robBrick_[A-z]*-Brick");

    private Jaxb2ConfigurationAst() {
    }

    private static Block getTopBlock(BlockSet blockSet, String topBlockName) {
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

    public static ConfigurationAst blocks2OldConfig(BlockSet set, BlocklyDropdownFactory factory, String topBlockName, String sensorsPrefix) {
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        Block topBlock = Jaxb2ConfigurationAst.getTopBlock(set, topBlockName);
        List<Field> fields = Jaxb2Ast.extractFields(topBlock, (short) 255);
        Jaxb2ConfigurationAst.setWithOptField(fields, "WHEEL_DIAMETER", wd -> builder.setWheelDiameter(Float.parseFloat(wd)));
        Jaxb2ConfigurationAst.setWithOptField(fields, "TRACK_WIDTH", wd -> builder.setTrackWidth(Float.parseFloat(wd)));
        Jaxb2ConfigurationAst.setWithOptField(fields, "IP_ADDRESS", builder::setIpAddress);
        Jaxb2ConfigurationAst.setWithOptField(fields, "USERNAME", builder::setUserName);
        Jaxb2ConfigurationAst.setWithOptField(fields, "PASSWORD", builder::setPassword);

        List<ConfigurationComponent> allComponents = Jaxb2ConfigurationAst.extractOldConfigurationComponent(topBlock, factory, sensorsPrefix);

        return builder.addComponents(allComponents).build();
    }

    private static void setWithOptField(List<Field> fields, String name, Callback<? super String> callback) {
        String val = Jaxb2Ast.optField(fields, name);
        if ( val != null ) {
            callback.call(val);
        }
    }

    private static List<ConfigurationComponent> extractOldConfigurationComponent(Block topBlock, BlocklyDropdownFactory factory, String sensorsPrefix) {
        List<ConfigurationComponent> allComponents = new ArrayList<>();
        for ( Value value : Jaxb2Ast.extractValues(topBlock, (short) 255) ) {
            String portName = value.getName();
            String userDefinedName = portName.substring(1);
            boolean isActor = !portName.startsWith(sensorsPrefix);
            String blocklyName = value.getBlock().getType();
            List<Field> fields = Jaxb2Ast.extractFields(value.getBlock(), (short) 255);
            Map<String, String> properties = new HashMap<>();
            for ( Field field : fields ) {
                String fKey = field.getName();
                String fValue = field.getValue();
                properties.put(fKey, fValue);
            }
            ConfigurationComponent configComp =
                new ConfigurationComponentLeaf(
                    factory.getConfigurationComponentTypeByBlocklyName(blocklyName),
                    isActor,
                    portName,
                    userDefinedName,
                    properties,
                    Jaxb2Ast.extractBlockProperties(topBlock),
                    Jaxb2Ast.extractComment(topBlock),
                    0,
                    0);
            allComponents.add(configComp);
        }
        return allComponents;
    }

    public static ConfigurationAst blocks2NewConfig(BlockSet set, BlocklyDropdownFactory factory) {
        List<Instance> instances = set.getInstance();
        List<ConfigurationComponent> allComponents = new ArrayList<>();
        for ( Instance instance : instances ) {
            if ( instance.getBlock().size() != 0 && instance.getBlock().get(0).getStatement().size() != 0 ) {
                allComponents.add(Jaxb2ConfigurationAst.block2NewConfigComp(instance.getBlock().get(0), factory, instance.getX(), instance.getY()));
            } else {
                allComponents.add(Jaxb2ConfigurationAst.instance2NewConfigComp(instance, factory));
            }
        }

        return new ConfigurationAst.Builder()
            .setRobotType(set.getRobottype())
            .setXmlVersion(set.getXmlversion())
            .setDescription(set.getDescription())
            .setTags(set.getTags())
            .addComponents(allComponents)
            .build();
    }

    private static ConfigurationComponent block2NewConfigComp(Block block, BlocklyDropdownFactory factory, String x, String y) {
        String componentType = factory.getConfigurationComponentTypeByBlocklyName(block.getType());
        String userDefinedName = block.getField().get(0).getValue();
        Map<String, String> map = new LinkedHashMap<>();
        for ( int i = 1; i < block.getField().size(); i++ ) {
            map.put(block.getField().get(i).getName(), block.getField().get(i).getValue());
        }
        LinkedHashMap<String, List<ConfigurationComponent>> subcomponents = new LinkedHashMap<>();
        List<Statement> statements = block.getStatement();
        if ( statements.size() == 0 ) {
            return new ConfigurationComponentLeaf(
                componentType,
                true,
                userDefinedName,
                userDefinedName,
                map,
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block),
                Integer.parseInt(x),
                Integer.parseInt(y));
        }

        for ( Statement statement : statements ) {
            List<ConfigurationComponent> subBlocks = new ArrayList<>();
            for ( Block subBlock : statement.getBlock() ) {
                subBlocks.add(block2NewConfigComp(subBlock, factory, x, y));
            }
            subcomponents.put(statement.getName(), subBlocks);
        }

        return new ConfigurationComponentNode(
            componentType,
            true,
            userDefinedName,
            userDefinedName,
            map,
            Jaxb2Ast.extractBlockProperties(block),
            Jaxb2Ast.extractComment(block),
            Integer.parseInt(x),
            Integer.parseInt(y), subcomponents);
    }

    @SuppressWarnings("unchecked")
    private static ConfigurationComponent instance2NewConfigComp(Instance instance, BlocklyDropdownFactory factory) {
        Block firstBlock = instance.getBlock().get(0);
        String componentType = factory.getConfigurationComponentTypeByBlocklyName(firstBlock.getType());
        String userDefinedName = firstBlock.getField().get(0).getValue();
        Map<String, String> map = new LinkedHashMap<>();
        for ( int i = 1; i < firstBlock.getField().size(); i++ ) {
            map.put(firstBlock.getField().get(i).getName(), firstBlock.getField().get(i).getValue());
        }
        // TODO in order to avoid robot specific workarounds in the Robot project:
        // TODO this is workaround for the robot specific "robBrick_*-Brick" blocks, which have varying names for the "userDefinedPortName" from robot to robot
        // TODO this this should be removed if possible
        if ( BRICK_BLOCK_PATTERN.matcher(firstBlock.getType()).find() ) {
            String className =
                ConfigurationComponent.class.getPackage().getName()
                    + '.'
                    + StringUtils.capitalize(componentType.toLowerCase(Locale.ENGLISH))
                    + "ConfigurationComponent";
            try {
                Constructor<ConfigurationComponent> constructor =
                    (Constructor<ConfigurationComponent>) Class
                        .forName(className)
                        .getDeclaredConstructor(
                            String.class,
                            Boolean.TYPE,
                            String.class,
                            String.class,
                            Map.class,
                            BlocklyBlockProperties.class,
                            BlocklyComment.class,
                            Integer.TYPE,
                            Integer.TYPE);
                constructor.setAccessible(true);
                return constructor
                    .newInstance(
                        componentType,
                        true,
                        null,
                        userDefinedName,
                        map,
                        Jaxb2Ast.extractBlockProperties(firstBlock),
                        Jaxb2Ast.extractComment(firstBlock),
                        Integer.parseInt(instance.getX()),
                        Integer.parseInt(instance.getY()));
            } catch ( ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e ) {
                throw new DbcException("Could not find matching constructor for " + className, e);
            }
        } else {
            return new ConfigurationComponentLeaf(
                componentType,
                true,
                userDefinedName,
                userDefinedName,
                map,
                Jaxb2Ast.extractBlockProperties(firstBlock),
                Jaxb2Ast.extractComment(firstBlock),
                Integer.parseInt(instance.getX()),
                Integer.parseInt(instance.getY()));
        }
    }
}
