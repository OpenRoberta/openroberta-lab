package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IBob3Visitor;

/**
 * This class represents the <b>mbedActions_leds_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * turning on the Led.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class BodyLEDAction<V> extends Action<V> {
    private final String ledState;
    private final String side;

    private BodyLEDAction(String side, String ledState, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BOB3_BODYLED"), properties, comment);
        this.ledState = ledState;
        this.side = side;
        setReadOnly();
    }

    /**
     * Creates instance of {@link BodyLEDAction}. This instance is read only and can not be modified.
     *
     * @param ledColor {@link ColorConst} color of the led; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BodyLEDAction}
     */
    private static <V> BodyLEDAction<V> make(String side, String ledState, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BodyLEDAction<>(side, ledState, properties, comment);
    }

    /**
     * @return {@link ColorConst} color of the led.
     */
    public String getledState() {
        return this.ledState;
    }

    @Override
    public String toString() {
        return "LedOnAction [ " + this.ledState + ", " + this.side + " ]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IBob3Visitor<V>) visitor).visitBodyLEDAction(this);
    }

    public String getSide() {
        return this.side;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String side = helper.extractField(fields, BlocklyConstants.LED + BlocklyConstants.SIDE);
        String ledState = helper.extractField(fields, BlocklyConstants.LED + BlocklyConstants.STATE);
        return BodyLEDAction.make(side, ledState, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.LED + BlocklyConstants.SIDE, this.side);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.LED + BlocklyConstants.STATE, this.ledState);
        return jaxbDestination;

    }
}
