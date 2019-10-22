package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.Led;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * This class represents the <b>naoActions_ledOff</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * turning all LEDs off.<br/>
 * <br/>
 */
public final class LedOff<V> extends Action<V> {

    private final Led led;

    private LedOff(Led led, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_OFF"), properties, comment);
        this.led = led;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedOffTest}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedOffTest}
     */
    private static <V> LedOff<V> make(Led led, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LedOff<V>(led, properties, comment);
    }

    @Override
    public String toString() {
        return "LedOff [led=" + this.led + "]";
    }

    public Led getLed() {
        return this.led;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitLedOff(this);
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
        String leds = helper.extractField(fields, BlocklyConstants.LED);

        return LedOff.make(Led.get(leds), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.LED, this.led.toString());

        return jaxbDestination;
    }
}
