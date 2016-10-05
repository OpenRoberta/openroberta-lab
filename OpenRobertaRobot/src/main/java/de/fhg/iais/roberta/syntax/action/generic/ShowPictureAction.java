package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
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
 * This class represents the <b>robActions_display_picture</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code showing a picture on the screen of the brick.<br/>
 * <br/>
 * The client must provide the name of the picture and x and y coordinates.
 */
public class ShowPictureAction<V> extends Action<V> {
    private final IShowPicture pic;
    private final Expr<V> x;
    private final Expr<V> y;

    private ShowPictureAction(IShowPicture pic, Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SHOW_PICTURE_ACTION"),properties, comment);
        Assert.isTrue(pic != null && x != null && y != null);
        this.pic = pic;
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ShowPictureAction}. This instance is read only and can not be modified.
     *
     * @param pic that will be printed on the display of the brick; must be <b>not</b> null,
     * @param x position where the picture will start; must be <b>not</b> null,
     * @param y position where the picture will start; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ShowPictureAction}
     */
    private static <V> ShowPictureAction<V> make(IShowPicture pic, Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ShowPictureAction<V>(pic, x, y, properties, comment);
    }

    /**
     * @return name of the picture that
     */
    public IShowPicture getPicture() {
        return this.pic;
    }

    /**
     * @return position x of the picture on the display.
     */
    public Expr<V> getX() {
        return this.x;
    }

    /**
     * @return position y of the picture on the display
     */
    public Expr<V> getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "ShowPictureAction [" + this.pic + ", " + this.x + ", " + this.y + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitShowPictureAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        List<Value> values = helper.extractValues(block, (short) 2);
        String pic = helper.extractField(fields, BlocklyConstants.PICTURE);
        Phrase<V> x = helper.extractValue(values, new ExprParam(BlocklyConstants.X_, Integer.class));
        Phrase<V> y = helper.extractValue(values, new ExprParam(BlocklyConstants.Y_, Integer.class));
        return ShowPictureAction.make(
            factory.getShowPicture(pic),
            helper.convertPhraseToExpr(x),
            helper.convertPhraseToExpr(y),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = getPicture().toString();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.PICTURE, fieldValue);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.X_, getX());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.Y_, getY());

        return jaxbDestination;

    }
}
