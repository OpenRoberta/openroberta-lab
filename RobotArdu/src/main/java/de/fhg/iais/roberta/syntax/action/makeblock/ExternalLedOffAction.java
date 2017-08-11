package de.fhg.iais.roberta.syntax.action.makeblock;

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
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.ArduAstVisitor;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>mbedActions_leds_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning on the Led.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ExternalLedOffAction<V> extends Action<V> {
    private String side;
    private final String port;

    private ExternalLedOffAction(String side, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("MAKEBLOCK_EXTERNAL_RGB_LED_OFF"), properties, comment);
        this.side = side;
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ExternalLedOffAction}. This instance is read only and can not be modified.
     *
     * @param ledColor {@link ColorConst} color of the led; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ExternalLedOffAction}
     */
    private static <V> ExternalLedOffAction<V> make(String side, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ExternalLedOffAction<>(side, port, properties, comment);
    }

    @Override
    public String toString() {
        return "LedOnAction [ ]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((ArduAstVisitor<V>) visitor).visitExternalLedOffAction(this);
    }

    public String getSide() {
        return this.side;
    }

    public String getPort() {
        return this.port;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String side = helper.extractField(fields, BlocklyConstants.LEDNUMBER);
        String port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        return ExternalLedOffAction.make(side, port, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.LEDNUMBER, this.side);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, this.port);
        return jaxbDestination;

    }
}
