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
 * This class represents the <b>mbedActions_motionkit_dual_set</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 */
public final class MotionKitDualSetAction<V> extends Action<V> {
    private final String directionLeft;
    private final String directionRight;

    /**
     * This constructor set the kind of the action object used in the AST (abstract syntax tree). All possible kinds can be found in {@link BlockType}.
     *
     * @param directionLeft the desired direction of the left motor
     * @param directionRight the desired direction of the right motor
     * @param properties of the block,
     * @param comment of the user for the specific block
     */
    private MotionKitDualSetAction(String directionLeft, String directionRight, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("MOTIONKIT_DUAL_SET_ACTION"), properties, comment);
        Assert.notNull(directionLeft);
        Assert.notNull(directionRight);
        this.directionLeft = directionLeft;
        this.directionRight = directionRight;
        setReadOnly();
    }

    public static <V> MotionKitDualSetAction<V> make(String directionLeft, String directionRight, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotionKitDualSetAction<>(directionLeft, directionRight, properties, comment);
    }

    public String getDirectionLeft() {
        return directionLeft;
    }

    public String getDirectionRight() {
        return directionRight;
    }

    @Override
    public String toString() {
        return "MotionKitDualSetAction [" + this.directionLeft + ", " + this.directionRight + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitMotionKitDualSetAction(this);
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
        String directionL = helper.extractField(fields, BlocklyConstants.DIRECTION_LEFT);
        String directionR = helper.extractField(fields, BlocklyConstants.DIRECTION_RIGHT);
        return MotionKitDualSetAction.make(factory.getMode(directionL), factory.getMode(directionR), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION_LEFT, this.directionLeft);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION_RIGHT, this.directionRight);
        return jaxbDestination;
    }
}
