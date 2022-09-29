package de.fhg.iais.roberta.syntax.expr.mbed;

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
 * This class represents the <b>mbedImage_image</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate an image.<br/>
 * The client must provide the value for every pixel of the display (5x5). <br>
 */
@NepoBasic(name = "IMAGE", category = "EXPR", blocklyNames = {"mbedImage_image"})
public final class Image extends Expr {
    public final String[][] image;

    public Image(String[][] image, BlocklyProperties properties) {
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

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 25);
        String[][] image = new String[5][5];
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                image[i][j] = Jaxb2Ast.extractField(fields, "P" + j + i);
            }
        }
        return new Image(image, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                Ast2Jaxb.addField(jaxbDestination, "P" + j + i, this.image[i][j]);
            }
        }
        return jaxbDestination;
    }
}
