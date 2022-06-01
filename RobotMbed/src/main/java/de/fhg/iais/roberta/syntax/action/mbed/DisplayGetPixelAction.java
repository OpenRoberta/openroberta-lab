package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>mbedActions_leds_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * turning on the Led.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class DisplayGetPixelAction<V> extends Action<V> {
    private final Expr<V> x;
    private final Expr<V> y;

    private DisplayGetPixelAction(Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DISPLAY_GET_PIXEL"), properties, comment);
        Assert.notNull(x);
        Assert.notNull(y);
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DisplayGetPixelAction}. This instance is read only and can not be modified.
     *
     * @param brightness of the display; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DisplayGetPixelAction}
     */
    public static <V> DisplayGetPixelAction<V> make(Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DisplayGetPixelAction<>(x, y, properties, comment);
    }

    /**
     * @return x of the pixel.
     */
    public Expr<V> getX() {
        return this.x;
    }

    /**
     * @return y of the pixel.
     */
    public Expr<V> getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "DisplaySetBrightnessAction [ " + this.x + ", " + this.y + " ]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);

        Phrase<V> x = helper.extractValue(values, new ExprParam(BlocklyConstants.X, BlocklyType.NUMBER_INT));
        Phrase<V> y = helper.extractValue(values, new ExprParam(BlocklyConstants.Y, BlocklyType.NUMBER_INT));
        return DisplayGetPixelAction
            .make(Jaxb2Ast.convertPhraseToExpr(x), Jaxb2Ast.convertPhraseToExpr(y), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.X, this.x);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Y, this.y);

        return jaxbDestination;

    }
}
