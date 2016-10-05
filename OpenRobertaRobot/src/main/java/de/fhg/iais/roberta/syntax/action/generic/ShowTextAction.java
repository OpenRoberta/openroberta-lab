package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robActions_display_text</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code showing a text message on the screen of the brick.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, Expr, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * <br>
 * The client must provide the message and x and y coordinates.
 */
public class ShowTextAction<V> extends Action<V> {
    private final Expr<V> msg;
    private final Expr<V> x;
    private final Expr<V> y;

    private ShowTextAction(Expr<V> msg, Expr<V> column, Expr<V> row, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SHOW_TEXT_ACTION"),properties, comment);
        Assert.isTrue(msg != null && column != null && row != null);
        this.msg = msg;
        this.x = column;
        this.y = row;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ShowTextAction}. This instance is read only and can not be modified.
     *
     * @param msg that will be printed on the display of the brick; must be <b>not</b> null,
     * @param x position where the message will start; must be <b>not</b> null,
     * @param y position where the message will start; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ShowTextAction}
     */
    private static <V> ShowTextAction<V> make(Expr<V> msg, Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ShowTextAction<V>(msg, x, y, properties, comment);
    }

    /**
     * @return the message.
     */
    public Expr<V> getMsg() {
        return this.msg;
    }

    /**
     * @return position x of the picture on the display.
     */
    public Expr<V> getX() {
        return this.x;
    }

    /**
     * @return position y of the picture on the display.
     */
    public Expr<V> getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "ShowTextAction [" + this.msg + ", " + this.x + ", " + this.y + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitShowTextAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 3);
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, String.class));
        Phrase<V> col = helper.extractValue(values, new ExprParam(BlocklyConstants.COL_, Integer.class));
        Phrase<V> row = helper.extractValue(values, new ExprParam(BlocklyConstants.ROW_, Integer.class));
        return ShowTextAction.make(
            helper.convertPhraseToExpr(msg),
            helper.convertPhraseToExpr(col),
            helper.convertPhraseToExpr(row),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.OUT, getMsg());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.COL_, getX());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.ROW_, getY());

        return jaxbDestination;
    }

}
