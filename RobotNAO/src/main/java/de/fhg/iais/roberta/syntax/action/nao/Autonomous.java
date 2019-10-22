package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.general.WorkingState;
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
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

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

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitAutonomous(this);
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

        String onOff = helper.extractField(fields, BlocklyConstants.MODE);

        return Autonomous.make(WorkingState.get(onOff), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public String toString() {
        return "SetStiffness [" + ", " + this.onOff + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, this.onOff.toString());

        return jaxbDestination;
    }
}