package de.fhg.iais.roberta.syntax.sensors.raspberrypi;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IRaspberryPiVisitor;

/**
 * This class represents the <b>mbedActions_ledBar_set</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for turning on the Grove LED Bar v2.0.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class SlotSensor<V> extends Expr<V> {
    private final String value;

    private SlotSensor(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SLOT_SENSING"), properties, comment);
        Assert.notNull(value);
        this.value = value;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SlotSensor}. This instance is read only and can not be modified.
     *
     * @param brightness of the display; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SlotSensor}
     */
    private static <V> SlotSensor<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SlotSensor<>(value, properties, comment);
    }

    /**
     * @return x of the pixel.
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "SlotSensor [ " + this.value + " ]";
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((IRaspberryPiVisitor<V>) visitor).visitSlotSensor(this);
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

        String value = helper.extractField(fields, BlocklyConstants.SLOT);
        return SlotSensor.make(value, helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.SLOT, this.value);
        return jaxbDestination;

    }

    @Override
    public int getPrecedence() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Assoc getAssoc() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType getVarType() {
        // TODO Auto-generated method stub
        return null;
    }
}
