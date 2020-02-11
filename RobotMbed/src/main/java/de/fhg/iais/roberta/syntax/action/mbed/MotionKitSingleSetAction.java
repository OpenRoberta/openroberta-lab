package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockType;
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
 * This class represents the <b>mbedActions_motionkit_single_set</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 */
public final class MotionKitSingleSetAction<V> extends Action<V> {
    private final String port;
    private final String direction;

    /**
     * This constructor set the kind of the action object used in the AST (abstract syntax tree). All possible kinds can be found in {@link BlockType}.
     *
     * @param port the desired motor(s)
     * @param direction the desired direction of the motor
     * @param properties of the block,
     * @param comment of the user for the specific block
     */
    private MotionKitSingleSetAction(String port, String direction, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("MOTIONKIT_SINGLE_SET_ACTION"), properties, comment);
        Assert.notNull(port);
        Assert.notNull(direction);
        this.port = port;
        this.direction = direction;
        setReadOnly();
    }

    public static <V> MotionKitSingleSetAction<V> make(String port, String direction, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotionKitSingleSetAction<>(port, direction, properties, comment);
    }

    public String getPort() {
        return port;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "MotionKitSingleSetAction [" + this.port + ", " + this.direction + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitMotionKitSingleSetAction(this);
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
        String port = helper.extractField(fields, BlocklyConstants.MOTORPORT);
        String direction = helper.extractField(fields, BlocklyConstants.DIRECTION);
        return MotionKitSingleSetAction.make(factory.sanitizePort(port), factory.getMode(direction), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MOTORPORT, this.port);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.direction);
        return jaxbDestination;
    }
}
