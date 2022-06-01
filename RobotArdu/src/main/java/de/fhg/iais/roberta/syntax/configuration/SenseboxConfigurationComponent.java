package de.fhg.iais.roberta.syntax.configuration;

import java.math.BigInteger;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;

/**
 * Just to override the name of the "usedDefinedPortName" field and add the mutation to the generic {@link ConfigurationComponent}. TODO should be removed if
 * possible
 */
public final class SenseboxConfigurationComponent extends ConfigurationComponent {
    private SenseboxConfigurationComponent(
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
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf((this.getComponentProperties().size() / 2)));
        destination.setMutation(mutation);
        Ast2Jaxb.addField(destination, "BOX_ID", this.getUserDefinedPortName());
        this.getComponentProperties().forEach((key, value) -> Ast2Jaxb.addField(destination, key, value));
        return destination;
    }
}
