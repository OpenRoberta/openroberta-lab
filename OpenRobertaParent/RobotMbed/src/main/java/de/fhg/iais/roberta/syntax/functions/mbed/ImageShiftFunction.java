package de.fhg.iais.roberta.syntax.functions.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.general.IDirection;
import de.fhg.iais.roberta.mode.general.Direction;
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
 * This class represents <b>mbedImage_shift</b> blocks from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(Image, Direction, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ImageShiftFunction<V> extends Function<V> {
    private final Expr<V> image;
    private final Expr<V> positions;
    private final IDirection shiftDirection;

    private ImageShiftFunction(Expr<V> image, Expr<V> positions, IDirection shiftDirection, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("IMAGE_SHIFT"), properties, comment);
        Assert.notNull(image);
        Assert.notNull(positions);
        Assert.notNull(shiftDirection);
        this.image = image;
        this.shiftDirection = shiftDirection;
        this.positions = positions;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ImageShiftFunction}. This instance is read only and can not be modified.
     *
     * @param image to be shifted; must be <b>not</b> null,
     * @param positions to be shifted; must be <b>not</b> null,
     * @param shiftDirection direction of shifting {@link Direction}; must be <b>not</b> null,,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link ImageShiftFunction}
     */
    public static <V> ImageShiftFunction<V> make(
        Expr<V> image,
        Expr<V> positions,
        IDirection shiftDirection,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new ImageShiftFunction<>(image, positions, shiftDirection, properties, comment);
    }

    /**
     * @return image to be shifted
     */
    public Expr<V> getImage() {
        return this.image;
    }

    /**
     * @return direction of shifting {@link IDirection}
     */
    public IDirection getShiftDirection() {
        return this.shiftDirection;
    }

    public Expr<V> getPositions() {
        return this.positions;
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
        return ((MbedAstVisitor<V>) visitor).visitImageShiftFunction(this);

    }

    @Override
    public String toString() {
        return "ImageShiftFunction [" + this.image + ", " + this.positions + ", " + this.shiftDirection + "]";
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
        IDirection shiftingDirection = factory.getDirection(helper.extractField(fields, BlocklyConstants.OP));
        Phrase<V> image = helper.extractValue(values, new ExprParam(BlocklyConstants.A, BlocklyType.PREDEFINED_IMAGE));
        Phrase<V> numberOfPositions = helper.extractValue(values, new ExprParam(BlocklyConstants.B, BlocklyType.NUMBER_INT));
        return ImageShiftFunction.make(
            helper.convertPhraseToExpr(image),
            helper.convertPhraseToExpr(numberOfPositions),
            shiftingDirection,
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.OP, this.shiftDirection.toString());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.A, this.image);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.B, this.positions);

        return jaxbDestination;
    }

}
