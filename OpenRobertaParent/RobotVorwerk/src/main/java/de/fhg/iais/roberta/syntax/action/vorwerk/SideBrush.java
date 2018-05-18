package de.fhg.iais.roberta.syntax.action.vorwerk;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.vorwerk.VorwerkAstVisitor;

/**
 * This class represents the <b>vorwerkActions_brush_on</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for applying a posture<br/>
 * <br/>
 * The client must provide the {@link Expr} speed.
 */
public final class SideBrush<V> extends Action<V> {
    private final IWorkingState workingState;

    private SideBrush(IWorkingState workingState, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SIDE_BRUSH"), properties, comment);
        Assert.notNull(workingState, "Missing working state!");
        this.workingState = workingState;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SideBrush}. This instance is read only and can not be modified.
     *
     * @param speed {@link Expr} speed of the brush,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SideBrush}
     */
    private static <V> SideBrush<V> make(IWorkingState workingState, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SideBrush<V>(workingState, properties, comment);
    }

    public IWorkingState getWorkingState() {
        return this.workingState;
    }

    @Override
    public String toString() {
        return "SideBrush [" + this.workingState + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((VorwerkAstVisitor<V>) visitor).visitSideBrush(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);

        String workingState = helper.extractField(fields, "BRUSH_STATE");
        return SideBrush.make(WorkingState.get(workingState), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, "BRUSH_STATE", this.workingState.toString());

        return jaxbDestination;
    }
}
