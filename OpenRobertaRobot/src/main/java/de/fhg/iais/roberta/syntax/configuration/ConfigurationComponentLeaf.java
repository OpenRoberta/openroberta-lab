package de.fhg.iais.roberta.syntax.configuration;

import java.util.Map;

import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

public class ConfigurationComponentLeaf extends ConfigurationComponent {

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
