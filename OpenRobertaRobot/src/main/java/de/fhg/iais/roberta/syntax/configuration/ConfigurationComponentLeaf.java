package de.fhg.iais.roberta.syntax.configuration;

import java.util.Map;

import de.fhg.iais.roberta.util.ast.BlocklyProperties;

public final class ConfigurationComponentLeaf extends ConfigurationComponent {

    public ConfigurationComponentLeaf(
        String componentType,
        String category,
        String internalPortName,
        String userDefinedName,
        Map<String, String> componentProperties,
        BlocklyProperties properties,

        int x,
        int y) {
        super(componentType, category, internalPortName, userDefinedName, componentProperties, properties, x, y);
    }
}
