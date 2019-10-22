package de.fhg.iais.roberta.syntax.action.raspberrypi;

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
public class LedGetAction<V> extends Action<V> {
    private final String port;

    private LedGetAction(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_GET_ACTION"), properties, comment);
        Assert.notNull(port);
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedGetAction}. This instance is read only and can not be modified.
     *
     * @param brightness of the display; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedGetAction}
     */
    private static <V> LedGetAction<V> make(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LedGetAction<>(port, properties, comment);
    }

    /**
     * @return x of the pixel.
     */
    public String getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "LedSetAction [ " + this.port + " ]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IRaspberryPiVisitor<V>) visitor).visitLedGetAction(this);
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

        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT);
        return LedGetAction.make(port, helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);

        return jaxbDestination;

    }
}
