package de.fhg.iais.roberta.syntax.expressions.arduino;

import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

/**
 * This class represents the <b>makeblockColours</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate color.<br/>
 * <br>
 * The client must provide the value for each color channel. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, Expr, Expr, BlocklyProperties, BlocklyComment)}.<br>
 */
@NepoBasic(name = "LED_MATRIX_IMAGE", category = "EXPR", blocklyNames = {"mBotImage_image"})
public final class LEDMatrixImage extends Expr {
    public final static int X = 16;
    public final static int Y = 8;
    public final String[][] image;

    public LEDMatrixImage(String[][] image, BlocklyProperties properties) {
        super(properties);
        this.image = image;
        setReadOnly();
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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) (X * Y));
        String[][] image = new String[X][Y];
        for ( int i = 0; i < X; i++ ) {
            for ( int j = 0; j < Y; j++ ) {
                image[i][j] = Jaxb2Ast.extractField(fields, "P" + i + (Y - 1 - j));
            }
        }
        return new LEDMatrixImage(image, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        for ( int i = 0; i < X; i++ ) {
            for ( int j = 0; j < Y; j++ ) {
                Ast2Jaxb.addField(jaxbDestination, "P" + i + (Y - 1 - j), this.image[i][j]);
            }
        }
        return jaxbDestination;
    }
}
