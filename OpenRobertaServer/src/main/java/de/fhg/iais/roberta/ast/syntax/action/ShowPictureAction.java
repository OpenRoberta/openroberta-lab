package de.fhg.iais.roberta.ast.syntax.action;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_display_picture</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code showing a picture on the screen of the brick.<br/>
 * <br/>
 * The client must provide the name of the picture and x and y coordinates.
 */
public class ShowPictureAction<V> extends Action<V> {
    private final ShowPicture pic;
    private final Expr<V> x;
    private final Expr<V> y;

    private ShowPictureAction(ShowPicture pic, Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.SHOW_PICTURE_ACTION, properties, comment);
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
    private static <V> ShowPictureAction<V> make(ShowPicture pic, Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ShowPictureAction<V>(pic, x, y, properties, comment);
    }

    /**
     * @return name of the picture that
     */
    public ShowPicture getPicture() {
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
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        List<Value> values = helper.extractValues(block, (short) 2);
        String pic = helper.extractField(fields, BlocklyConstants.PICTURE);
        Phrase<V> x = helper.extractValue(values, new ExprParam(BlocklyConstants.X_, Integer.class));
        Phrase<V> y = helper.extractValue(values, new ExprParam(BlocklyConstants.Y_, Integer.class));
        return ShowPictureAction.make(
            ShowPicture.get(pic),
            helper.convertPhraseToExpr(x),
            helper.convertPhraseToExpr(y),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = getPicture().name();
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.PICTURE, fieldValue);
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.X_, getX());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.Y_, getY());

        return jaxbDestination;

    }
}
