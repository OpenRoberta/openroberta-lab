package de.fhg.iais.roberta.components;

import java.util.Map;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;

public class ConfigurationComponentLeaf extends ConfigurationComponent{

    public ConfigurationComponentLeaf(
        String componentType,
        boolean isActor,
        String internalPortName,
        String userDefinedName,
        Map<String, String> componentProperties,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int x,
        int y) {
        super(componentType, isActor, internalPortName, userDefinedName, componentProperties, properties, comment, x, y);
    }
}
