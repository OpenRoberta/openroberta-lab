package de.fhg.iais.roberta.syntax.expr;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.CalliopeAstVisitor;

/**
 * This class represents the <b>math_constant</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate mathematical constant. See enum {@link Const} for all defined constants.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(Const, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class PredefinedImage<V> extends Expr<V> {
    private final PredefinedImageNames imageName;

    private PredefinedImage(PredefinedImageNames imageName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PREDEFINED_IMAGE"), properties, comment);
        Assert.notNull(imageName);
        this.imageName = imageName;
        setReadOnly();
    }

    /**
     * creates instance of {@link PredefinedImage}. This instance is read only and can not be modified.
     *
     * @param imageName, see enum {@link PredefinedImage} for all defined images; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PredefinedImage}
     */
    public static <V> PredefinedImage<V> make(PredefinedImageNames predefinedImage, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PredefinedImage<>(predefinedImage, properties, comment);
    }

    /**
     * @return name of the image.
     */
    public PredefinedImageNames getImageName() {
        return this.imageName;
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
        return BlocklyType.STRING;
    }

    @Override
    public String toString() {
        return "PredefinedImage [" + this.imageName + "]";
    }

    /**
     * This enum defines all possible predefined images.
     */
    public static enum PredefinedImageNames {
        HEART(),
        HEART_SMALL(),
        HAPPY(),
        SMILE(),
        SAD(),
        CONFUSED(),
        ANGRY(),
        ASLEEP(),
        SURPRISED(),
        SILLY(),
        FABULOUS(),
        MEH(),
        ES(),
        NO(),
        TRIANGLE(),
        TRIANGLE_LEFT(),
        CHESSBOARD(),
        DIAMOND(),
        DIAMOND_SMALL(),
        SQUARE(),
        SQUARE_SMALL(),
        RABBIT(),
        COW(),
        MUSIC_CROTCHET(),
        MUSIC_QUAVER(),
        MUSIC_QUAVERS(),
        PITCHFORK(),
        XMAS(),
        PACMAN(),
        TARGET(),
        TSHIRT(),
        ROLLERSKATE(),
        DUCK(),
        HOUSE(),
        TORTOISE(),
        BUTTERFLY(),
        STICKFIGURE(),
        GHOST(),
        SWORD(),
        GIRAFFE(),
        SKULL(),
        UMBRELLA(),
        SNAKE();

        private final String[] values;

        private PredefinedImageNames(String... values) {
            this.values = values;
        }

        /**
         * get predifined image from {@link PredefinedImageNames} from string parameter. It is possible for one image to have multiple string mappings.
         * Throws exception if the constant does not exists.
         *
         * @param name of the image
         * @return image from the enum {@link PredefinedImageNames}
         */
        public static PredefinedImageNames get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid predifined image: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( PredefinedImageNames co : PredefinedImageNames.values() ) {
                if ( co.toString().equals(sUpper) ) {
                    return co;
                }
                for ( String value : co.values ) {
                    if ( sUpper.equals(value) ) {
                        return co;
                    }
                }
            }
            throw new DbcException("Invalid predifined image: " + s);
        }
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((CalliopeAstVisitor<V>) visitor).visitPredefinedImage(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String field = helper.extractField(fields, BlocklyConstants.IMAGE);
        return PredefinedImage.make(PredefinedImageNames.get(field), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.IMAGE, this.imageName.toString());
        return jaxbDestination;
    }
}
