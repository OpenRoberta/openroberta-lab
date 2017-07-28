package de.fhg.iais.roberta.syntax.expr;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.Bob3AstVisitor;

/**
 * This class represents the <b>makeblockColours</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate color.<br/>
 * <br>
 * The client must provide the value for each color channel. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, Expr, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class RgbColor<V> extends Expr<V> {
    private final Expr<V> R;
    private final Expr<V> G;
    private final Expr<V> B;

    private RgbColor(Expr<V> R, Expr<V> G, Expr<V> B, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RGB_COLOR"), properties, comment);
        this.R = R;
        this.G = G;
        this.B = B;
        setReadOnly();
    }

    /**
     * creates instance of {@link RgbColor}. This instance is read only and can not be modified.
     *
     * @param value that the boolean constant will have,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link RgbColor}
     */
    public static <V> RgbColor<V> make(Expr<V> R, Expr<V> G, Expr<V> B, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RgbColor<V>(R, G, B, properties, comment);
    }

    public Expr<V> getR() {
        return this.R;
    }

    public Expr<V> getG() {
        return this.G;
    }

    public Expr<V> getB() {
        return this.B;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.BOOLEAN;
    }

    @Override
    public String toString() {
        return "RgbColor [" + this.R + ", " + this.G + ", " + this.B + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((Bob3AstVisitor<V>) visitor).visitRgbColor(this);

    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values;
        boolean oldVersion = false;
        try {
            values = helper.extractValues(block, (short) 4);
        } catch ( Exception e ) {
            oldVersion = true;
            values = helper.extractValues(block, (short) 3);
        }

        Phrase<V> red = helper.extractValue(values, new ExprParam(BlocklyConstants.RED, BlocklyType.NUMBER_INT));
        Phrase<V> green = helper.extractValue(values, new ExprParam(BlocklyConstants.GREEN, BlocklyType.NUMBER_INT));
        Phrase<V> blue = helper.extractValue(values, new ExprParam(BlocklyConstants.BLUE, BlocklyType.NUMBER_INT));
        return RgbColor.make(
            helper.convertPhraseToExpr(red),
            helper.convertPhraseToExpr(green),
            helper.convertPhraseToExpr(blue),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.RED, this.R);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.GREEN, this.G);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.BLUE, this.B);

        return jaxbDestination;
    }

}
