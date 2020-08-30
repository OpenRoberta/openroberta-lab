package de.fhg.iais.roberta.syntax.expressions.arduino;

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
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

/**
 * This class represents the <b>makeblockColours</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate color.<br/>
 * <br>
 * The client must provide the value for each color channel. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, Expr, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class LEDMatrixImage<V> extends Expr<V> {
    private final static int X = 16;
    private final static int Y = 8;
    private final String[][] image;

    private LEDMatrixImage(String[][] image, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_MATRIX_IMAGE"), properties, comment);
        this.image = image;
        setReadOnly();
    }

    /**
     * creates instance of {@link LEDMatrixImage}. This instance is read only and can not be modified.
     *
     * @param image ,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LEDMatrixImage}
     */
    public static <V> LEDMatrixImage<V> make(String[][] image, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LEDMatrixImage<>(image, properties, comment);
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
        StringBuilder returnString = new StringBuilder();
        returnString.append("Image [ ");
        for ( int i = 0; i < X; ++i ) {
            returnString.append(Arrays.toString(this.image[i]));
            returnString.append("\n");
        }
        returnString.replace(returnString.lastIndexOf("\n"), returnString.lastIndexOf("\n"), " ]");
        return returnString.toString();
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbotVisitor<V>) visitor).visitLEDMatrixImage(this);

    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) (X*Y));
        String[][] image = new String[X][Y];
        for ( int i = 0; i < X; i++ ) {
            for ( int j = 0; j < Y; j++ ) {
                image[i][j] = helper.extractField(fields, "P" + i + (Y-1-j));
            }
        }
        return LEDMatrixImage.make(image, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        for ( int i = 0; i < X; i++ ) {
            for ( int j = 0; j < Y; j++ ) {
                Ast2JaxbHelper.addField(jaxbDestination, "P" + i + (Y - 1 - j), this.image[i][j]);
            }
        }
        return jaxbDestination;
    }
}
