package de.fhg.iais.roberta.syntax.actors.edison;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IEdisonVisitor;

public class ReceiveIRAction<V> extends Action<V> {

    private ReceiveIRAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("IR_RECV"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link ReceiveIRAction}. This instance is read only and can not be modified.
     *
     * @param ledColor {@link ColorConst} color of the led; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ReceiveIRAction}
     */
    private static <V> ReceiveIRAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ReceiveIRAction<>(properties, comment);
    }

    @Override
    public String toString() {
        return "ReceiveIRAction";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IEdisonVisitor<V>) visitor).visitReceiveIRAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return ReceiveIRAction.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;

    }
}