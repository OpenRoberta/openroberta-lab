package de.fhg.iais.roberta.syntax.action.nxt.addition;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NxtAstVisitor;

/**
 * This class represents the <b>robActions_display_text</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * showing a text message on the screen of the brick.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, Expr, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * <br>
 * The client must provide the message and x and y coordinates.
 */
public class ShowHelloWorldAction<V> extends Action<V> {

    private ShowHelloWorldAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("HELLO_WORLD"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link ShowHelloWorldAction}. This instance is read only and can not be modified.
     *
     * @param msg that will be printed on the display of the brick; must be <b>not</b> null,
     * @param x position where the message will start; must be <b>not</b> null,
     * @param y position where the message will start; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ShowHelloWorldAction}
     */
    private static <V> ShowHelloWorldAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ShowHelloWorldAction<>(properties, comment);
    }

    @Override
    public String toString() {
        return "ShowHelloWorldAction [HelloWorld]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NxtAstVisitor<V>) visitor).visitShowHelloWorldAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        return ShowHelloWorldAction.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }

}
