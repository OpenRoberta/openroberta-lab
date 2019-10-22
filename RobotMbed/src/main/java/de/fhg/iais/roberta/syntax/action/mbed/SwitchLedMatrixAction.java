package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

/**
 * This class represents the <b>mbedActions_pin_set_pull</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for reading values from a given pin.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class SwitchLedMatrixAction<V> extends Action<V> {
    private final boolean activated;

    private SwitchLedMatrixAction(boolean activated, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SWITCH_LED_MATRIX"), properties, comment);
        this.activated = activated;
        setReadOnly();
    }

    /**
     * Create object of the class {@link SwitchLedMatrixAction}.
     *
     * @param state state of the leds
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link SwitchLedMatrixAction}
     */
    public static <V> SwitchLedMatrixAction<V> make(boolean activated, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SwitchLedMatrixAction<>(activated, properties, comment);
    }

    public boolean isActivated() {
        return this.activated;
    }

    @Override
    public String toString() {
        return "SwitchLedMatrixAction [" + this.activated + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitSwitchLedMatrixAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);

        boolean activated = helper.extractField(fields, BlocklyConstants.STATE).equals("ON");
        return SwitchLedMatrixAction.make(activated, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.STATE, (this.activated) ? SC.ON : SC.OFF);

        return jaxbDestination;
    }
}
