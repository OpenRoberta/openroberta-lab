package de.fhg.iais.roberta.syntax.configuration;

import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;

/**
 * Just to override the name of the "usedDefinedPortName" field of the generic {@link ConfigurationComponent}. TODO: should be removed if possible
 */
public final class WedoConfigurationComponent extends ConfigurationComponent {
    private WedoConfigurationComponent(
        String componentType,
        boolean isActor,
        String portName,
        String userDefinedName,
        Map<String, String> componentProperties,
        BlocklyProperties properties,

        int x,
        int y) {
        super(componentType, isActor, portName, userDefinedName, componentProperties, properties, x, y);
    }

    @Override
    public Block ast2xml() {
        Block destination = new Block();
        Ast2Jaxb.setBasicProperties(this, destination);
        Ast2Jaxb.addField(destination, "VAR", this.userDefinedPortName);
        this.getComponentProperties().forEach((key, value) -> Ast2Jaxb.addField(destination, key, value));
        return destination;
    }
}
