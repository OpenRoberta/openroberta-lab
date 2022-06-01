package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.Led;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

/**
 * This class represents the <b>naoActions_ledReset</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * resetting all LEDs to their standard state.<br/>
 * <br/>
 */
public final class LedReset<V> extends Action<V> {

    private final Led led;

    private LedReset(Led led, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_RESET"), properties, comment);
        this.led = led;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedReset}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedReset}
     */
    private static <V> LedReset<V> make(Led led, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LedReset<V>(led, properties, comment);
    }

    public Led getLed() {
        return this.led;
    }

    @Override
    public String toString() {
        return "LedReset [led=" + this.led + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String leds = Jaxb2Ast.extractField(fields, BlocklyConstants.LED);

        return LedReset.make(Led.get(leds), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LED, this.led.toString());

        return jaxbDestination;
    }
}
