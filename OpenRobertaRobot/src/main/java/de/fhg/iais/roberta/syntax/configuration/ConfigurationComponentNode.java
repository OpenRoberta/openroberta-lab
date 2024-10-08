package de.fhg.iais.roberta.syntax.configuration;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

public final class ConfigurationComponentNode extends ConfigurationComponent {
    private final LinkedHashMap<String, List<ConfigurationComponent>> subComponents;

    public ConfigurationComponentNode(
        String componentType,
        String category,
        String internalPortName,
        String userDefinedName,
        Map<String, String> componentProperties,
        BlocklyProperties properties,

        int x,
        int y,
        LinkedHashMap<String, List<ConfigurationComponent>> subComponents) {
        super(componentType, category, internalPortName, userDefinedName, componentProperties, properties, x, y);
        this.subComponents = subComponents;
    }

    @Override
    public LinkedHashMap<String, List<ConfigurationComponent>> getSubComponents() {
        return subComponents;
    }

    @Override
    public List<Block> ast2xml() {
        Block destination = new Block();
        Ast2Jaxb.setBasicProperties(this, destination);
        Ast2Jaxb.addField(destination, "NAME", this.userDefinedPortName);
        subComponents.forEach((statement, subComponents) -> Ast2Jaxb.addConfigurationComponents(destination, statement, subComponents));
        getComponentProperties().forEach((key, value) -> Ast2Jaxb.addField(destination, key, value));
        return Collections.singletonList(destination);
    }
    
    @Override
    public boolean hasSubComponents() {
        return true;
    }
}
