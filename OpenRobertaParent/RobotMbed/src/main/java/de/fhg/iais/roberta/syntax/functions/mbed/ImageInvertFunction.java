package de.fhg.iais.roberta.syntax.functions.mbed;

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
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;

/**
 * This class represents the <b>mbedImage_invert</b> blocks from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide image to be inverted. <br>
 * To create an instance from this class use the method {@link #make(Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ImageInvertFunction<V> extends Function<V> {
    private final Expr<V> image;

    private ImageInvertFunction(Expr<V> image, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("IMAGE_INVERT"), properties, comment);
        Assert.notNull(image);

        this.image = image;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ImageInvertFunction}. This instance is read only and can not be modified.
     *
     * @param image ; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link ImageInvertFunction}
     */
    public static <V> ImageInvertFunction<V> make(Expr<V> image, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ImageInvertFunction<>(image, properties, comment);
    }

    /**
     * @return image under invertion
     */
    public Expr<V> getImage() {
        return this.image;
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.LEFT;
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.VOID;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitImageInvertFunction(this);

    }

    @Override
    public String toString() {
        return "ImageInvertFunction [" + this.image + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> image = helper.extractValue(values, new ExprParam(BlocklyConstants.VAR, BlocklyType.PREDEFINED_IMAGE));
        return ImageInvertFunction.make(helper.convertPhraseToExpr(image), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VAR, this.image);
        return jaxbDestination;
    }

}
