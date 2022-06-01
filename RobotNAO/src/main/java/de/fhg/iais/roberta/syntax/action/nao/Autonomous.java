package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>naoActions_Autonomous</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * toggling the state of autonomous life.<br/>
 * <br/>
 */
public final class Autonomous<V> extends Action<V> {

    private final WorkingState onOff;

    private Autonomous(WorkingState onOff, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("AUTONOMOUS"), properties, comment);
        Assert.notNull(onOff, "Missing onOff in Autonomous block!");
        this.onOff = onOff;
        setReadOnly();
    }

    /**
     * Creates instance of {@link Autonomous}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Autonomous}
     */
    private static <V> Autonomous<V> make(WorkingState onOff, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Autonomous<>(onOff, properties, comment);
    }

    public WorkingState getOnOff() {
        return this.onOff;
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

        String onOff = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);

        return Autonomous.make(WorkingState.get(onOff), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public String toString() {
        return "SetStiffness [" + ", " + this.onOff + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.onOff.toString());

        return jaxbDestination;
    }
}