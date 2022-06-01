package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);

        boolean activated = Jaxb2Ast.extractField(fields, BlocklyConstants.STATE).equals("ON");
        return SwitchLedMatrixAction.make(activated, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.STATE, (this.activated) ? SC.ON : SC.OFF);

        return jaxbDestination;
    }
}
