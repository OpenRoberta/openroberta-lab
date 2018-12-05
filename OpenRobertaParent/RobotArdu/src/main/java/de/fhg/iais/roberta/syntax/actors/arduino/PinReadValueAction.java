package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class PinReadValueAction<V> extends Action<V> {
    private final String mode;
    private final String port;

    private PinReadValueAction(String mode, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("READ_FROM_PIN"), properties, comment);
        Assert.notNull(mode);
        Assert.notNull(port);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link PinWriteValue}.
     *
     * @param pin
     * @param valueType see {@link PinValue}
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link PinWriteValue}
     */
    public static <V> PinReadValueAction<V> make(String mode, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PinReadValueAction<>(mode, port, properties, comment);
    }

    public String getMode() {
        return this.mode;
    }

    public String getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "PinWriteValueSensor [" + this.mode + ", " + this.port + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 3);
        String port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        String mode = helper.extractField(fields, BlocklyConstants.MODE);
        return PinReadValueAction.make(factory.getMode(mode), factory.sanitizePort(port), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Mutation mutation = new Mutation();
        mutation.setProtocol(this.mode.toString());
        jaxbDestination.setMutation(mutation);
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, this.mode.toString());
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, this.port);
        return jaxbDestination;
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitPinReadValueAction(this);
    }

}
