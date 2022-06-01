package de.fhg.iais.roberta.syntax.configuration;

import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;

/**
 * Just to override the name of the "usedDefinedPortName" field of the generic {@link ConfigurationComponent}. TODO should be removed if possible
 */
public final class WedoConfigurationComponent extends ConfigurationComponent {
    private WedoConfigurationComponent(
        String componentType,
        boolean isActor,
        String portName,
        String userDefinedName,
        Map<String, String> componentProperties,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int x,
        int y) {
        super(componentType, isActor, portName, userDefinedName, componentProperties, properties, comment, x, y);
    }

    @Override
    public Block astToBlock() {
        Block destination = new Block();
        Ast2Jaxb.setBasicProperties(this, destination);
        Ast2Jaxb.addField(destination, "VAR", this.getUserDefinedPortName());
        this.getComponentProperties().forEach((key, value) -> Ast2Jaxb.addField(destination, key, value));
        return destination;
    }
}
