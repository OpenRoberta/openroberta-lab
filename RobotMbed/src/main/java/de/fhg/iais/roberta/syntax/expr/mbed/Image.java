package de.fhg.iais.roberta.syntax.expr.mbed;

import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

/**
 * This class represents the <b>mbedImage_image</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate image.<br/>
 * <br>
 * The client must provide the value for every pixel of the display (5x5). <br>
 * <br>
 * To create an instance from this class use the method {@link #make(String..., BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class Image<V> extends Expr<V> {
    private final String[][] image;

    private Image(String[][] image, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("IMAGE"), properties, comment);
        this.image = image;
        setReadOnly();
    }

    /**
     * creates instance of {@link Image}. This instance is read only and can not be modified.
     *
     * @param image ,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Image}
     */
    public static <V> Image<V> make(String[][] image, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Image<>(image, properties, comment);
    }

    /**
     * @return get the string representation of the image.
     */
    public String[][] getImage() {
        return this.image;
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
        return BlocklyType.IMAGE;
    }

    @Override
    public String toString() {
        return "Image ["
            + Arrays.toString(this.image[0])
            + "\n"
            + Arrays.toString(this.image[1])
            + "\n"
            + Arrays.toString(this.image[2])
            + "\n"
            + Arrays.toString(this.image[3])
            + "\n"
            + Arrays.toString(this.image[4])
            + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitImage(this);

    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 25);
        String[][] image = new String[5][5];
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                image[i][j] = helper.extractField(fields, "P" + j + i);
            }
        }
        return Image.make(image, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                Ast2JaxbHelper.addField(jaxbDestination, "P" + j + i, this.image[i][j]);
            }
        }
        return jaxbDestination;
    }
}
