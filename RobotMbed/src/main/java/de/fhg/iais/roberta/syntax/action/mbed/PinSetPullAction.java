package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
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
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

/**
 * This class represents the <b>mbedActions_pin_set_pull</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for reading values from a given pin.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class PinSetPullAction<V> extends Action<V> {
    private final String pinPull;
    private final String port;

    private PinSetPullAction(String pinPull, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PIN_SET_PULL"), properties, comment);
        Assert.notNull(pinPull);
        Assert.notNull(port);
        this.pinPull = pinPull;
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link PinSetPullAction}.
     *
     * @param pin
     * @param valueType see {@link PinValue}
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link PinSetPullAction}
     */
    public static <V> PinSetPullAction<V> make(String pinPull, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PinSetPullAction<>(pinPull, port, properties, comment);
    }

    public String getMode() {
        return this.pinPull;
    }

    public String getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "PinSetPullAction [" + this.pinPull + ", " + this.port + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitPinSetPullAction(this);
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
        List<Field> fields = helper.extractFields(block, (short) 2);
        String port = helper.extractField(fields, BlocklyConstants.PIN_PORT);
        String pinPull = helper.extractField(fields, BlocklyConstants.PIN_PULL);
        return PinSetPullAction.make(factory.getMode(pinPull), factory.sanitizePort(port), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();

        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.PIN_PULL, this.pinPull.toString());
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.PIN_PORT, this.port);

        return jaxbDestination;
    }
}
