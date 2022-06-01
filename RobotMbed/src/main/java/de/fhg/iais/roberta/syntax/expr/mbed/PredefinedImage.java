package de.fhg.iais.roberta.syntax.expr.mbed;

import java.util.Locale;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoPhrase(containerType = "PREDEFINED_IMAGE")
public class PredefinedImage<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.IMAGE)
    public final String imageName;

    public PredefinedImage(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String imageName) {
        super(kind, properties, comment);
        Assert.notNull(imageName);
        this.imageName = imageName;
        setReadOnly();
    }

    public static <V> PredefinedImage<V> make(String predefinedImage, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PredefinedImage<V>(BlockTypeContainer.getByName("PREDEFINED_IMAGE"), properties, comment, predefinedImage);
    }

    public PredefinedImageNames getImageName() {
        return PredefinedImageNames.get(this.imageName);
    }

    public String getImageNameString() {
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

    public enum PredefinedImageNames {
          HEART(
              "0,255,0,255,0\\n" //
                  + "255,255,255,255,255\\n"
                  + "255,255,255,255,255\\n"
                  + "0,255,255,255,0\\n"
                  + "0,0,255,0,0\\n" ),
          HEART_SMALL(
              "0,0,0,0,0\\n" //
                  + "0,255,0,255,0\\n"
                  + "0,255,255,255,0\\n"
                  + "0,0,255,0,0\\n"
                  + "0,0,0,0,0\\n" ),
          HAPPY(
              "0,0,0,0,0\\n" //
                  + "0,255,0,255,0\\n"
                  + "0,0,0,0,0\\n"
                  + "255,0,0,0,255\\n"
                  + "0,255,255,255,0\\n" ),
          SMILE(
              "0,0,0,0,0\\n" //
                  + "0,0,0,0,0\\n"
                  + "0,0,0,0,0\\n"
                  + "255,0,0,0,255\\n"
                  + "0,255,255,255,0\\n" ),
          SAD(
              "0,0,0,0,0\\n" //
                  + "0,255,0,255,0\\n"
                  + "0,0,0,0,0\\n"
                  + "0,255,255,255,0\\n"
                  + "255,0,0,0,255\\n" ),
          CONFUSED(
              "0,0,0,0,0\\n" //
                  + "0,255,0,255,0\\n"
                  + "0,0,0,0,0\\n"
                  + "0,255,0,255,0\\n"
                  + "255,0,255,0,255\\n" ),
          ANGRY(
              "255,0,0,0,255\\n" //
                  + "0,255,0,255,0\\n"
                  + "0,0,0,0,0\\n"
                  + "255,255,255,255,255\\n"
                  + "255,0,255,0,255\\n" ),
          ASLEEP(
              "0,0,0,0,0\\n" //
                  + "255,255,0,255,255\\n"
                  + "0,0,0,0,0\\n"
                  + "0,255,255,255,0\\n"
                  + "0,0,0,0,0\\n" ),
          SURPRISED(
              "0,255,0,255,0\\n" //
                  + "0,0,0,0,0\\n"
                  + "0,0,255,0,0\\n"
                  + "0,255,0,255,0\\n"
                  + "0,0,255,0,0\\n" ),
          SILLY(
              "255,0,0,0,255\\n" //
                  + "0,0,0,0,0\\n"
                  + "255,255,255,255,255\\n"
                  + "0,0,255,0,255\\n"
                  + "0,0,255,255,255\\n" ),
          FABULOUS(
              "255,255,255,255,255\\n" //
                  + "255,255,0,255,255\\n"
                  + "0,0,0,0,0\\n"
                  + "0,255,0,255,0\\n"
                  + "0,255,255,255,0\\n" ),
          MEH(
              "0,255,0,255,0\\n" //
                  + "0,0,0,0,0\\n"
                  + "0,0,0,255,0\\n"
                  + "0,0,255,0,0\\n"
                  + "0,255,0,0,0\\n" ),
          YES(
              "0,0,0,0,0\\n" //
                  + "0,0,0,0,255\\n"
                  + "0,0,0,255,0\\n"
                  + "255,0,255,0,0\\n"
                  + "0,255,0,0,0\\n" ),
          NO(
              "255,0,0,0,255\\n" //
                  + "0,255,0,255,0\\n"
                  + "0,0,255,0,0\\n"
                  + "0,255,0,255,0\\n"
                  + "255,0,0,0,255\\n" ),
          TRIANGLE(
              "0,0,0,0,0\\n" //
                  + "0,0,255,0,0\\n"
                  + "0,255,0,255,0\\n"
                  + "255,255,255,255,255\\n"
                  + "0,0,0,0,0\\n" ),
          TRIANGLE_LEFT(
              "255,0,0,0,0\\n" //
                  + "255,255,0,0,0\\n"
                  + "255,0,255,0,0\\n"
                  + "255,0,0,255,0\\n"
                  + "255,255,255,255,255\\n" ),
          CHESSBOARD(
              "0,255,0,255,0\\n" //
                  + "255,0,255,0,255\\n"
                  + "0,255,0,255,0\\n"
                  + "255,0,255,0,255\\n"
                  + "0,255,0,255,0\\n" ),
          DIAMOND(
              "0,0,255,0,0\\n" //
                  + "0,255,0,255,0\\n"
                  + "255,0,0,0,255\\n"
                  + "0,255,0,255,0\\n"
                  + "0,0,255,0,0\\n" ),
          DIAMOND_SMALL(
              "0,0,0,0,0\\n" //
                  + "0,0,255,0,0\\n"
                  + "0,255,0,255,0\\n"
                  + "0,0,255,0,0\\n"
                  + "0,0,0,0,0\\n" ),
          SQUARE(
              "255,255,255,255,255\\n" //
                  + "255,0,0,0,255\\n"
                  + "255,0,0,0,255\\n"
                  + "255,0,0,0,255\\n"
                  + "255,255,255,255,255\\n" ),
          SQUARE_SMALL(
              "0,0,0,0,0\\n" //
                  + "0,255,255,255,0\\n"
                  + "0,255,0,255,0\\n"
                  + "0,255,255,255,0\\n"
                  + "0,0,0,0,0\\n" ),
          RABBIT(
              "255,0,255,0,0\\n" //
                  + "255,0,255,0,0\\n"
                  + "255,255,255,255,0\\n"
                  + "255,255,0,255,0\\n"
                  + "255,255,255,255,0\\n" ),
          COW(
              "255,0,0,0,255\\n" //
                  + "255,0,0,0,255\\n"
                  + "255,255,255,255,255\\n"
                  + "0,255,255,255,0\\n"
                  + "0,0,255,0,0\\n" ),
          MUSIC_CROTCHET(
              "0,0,255,0,0\\n" //
                  + "0,0,255,0,0\\n"
                  + "0,0,255,0,0\\n"
                  + "255,255,255,0,0\\n"
                  + "255,255,255,0,0\\n" ),
          MUSIC_QUAVER(
              "0,0,255,0,0\\n" //
                  + "0,0,255,255,0\\n"
                  + "0,0,255,0,255\\n"
                  + "255,255,255,0,0\\n"
                  + "255,255,255,0,0\\n" ),
          MUSIC_QUAVERS(
              "0,255,255,255,255\\n" //
                  + "0,255,0,0,255\\n"
                  + "0,255,0,0,255\\n"
                  + "255,255,0,255,255\\n"
                  + "255,255,0,255,255\\n" ),
          PITCHFORK(
              "255,0,255,0,255\\n" //
                  + "255,0,255,0,255\\n"
                  + "255,255,255,255,255\\n"
                  + "0,0,255,0,0\\n"
                  + "0,0,255,0,0\\n" ),
          XMAS(
              "0,0,255,0,0\\n" //
                  + "0,255,255,255,0\\n"
                  + "0,0,255,0,0\\n"
                  + "0,255,255,255,0\\n"
                  + "255,255,255,255,255\\n" ),
          PACMAN(
              "0,255,255,255,255\\n" //
                  + "255,255,0,255,0\\n"
                  + "255,255,255,0,0\\n"
                  + "255,255,255,255,0\\n"
                  + "0,255,255,255,255\\n" ),
          TARGET(
              "0,0,255,0,0\\n" //
                  + "0,255,255,255,0\\n"
                  + "255,255,0,255,255\\n"
                  + "0,255,255,255,0\\n"
                  + "0,0,255,0,0\\n" ),
          TSHIRT(
              "255,255,0,255,255\\n" //
                  + "255,255,255,255,255\\n"
                  + "0,255,255,255,0\\n"
                  + "0,255,255,255,0\\n"
                  + "0,255,255,255,0\\n" ),
          ROLLERSKATE(
              "0,0,0,255,255\\n" //
                  + "0,0,0,255,255\\n"
                  + "255,255,255,255,255\\n"
                  + "255,255,255,255,255\\n"
                  + "0,255,0,255,0\\n" ),
          DUCK(
              "0,255,255,0,0\\n" //
                  + "255,255,255,0,0\\n"
                  + "0,255,255,255,255\\n"
                  + "0,255,255,255,0\\n"
                  + "0,0,0,0,0\\n" ),
          HOUSE(
              "0,0,255,0,0\\n" //
                  + "0,255,255,255,0\\n"
                  + "255,255,255,255,255\\n"
                  + "0,255,255,255,0\\n"
                  + "0,255,0,255,0\\n" ),
          TORTOISE(
              "0,0,0,0,0\\n" //
                  + "0,255,255,255,0\\n"
                  + "255,255,255,255,255\\n"
                  + "0,255,0,255,0\\n"
                  + "0,0,0,0,0\\n" ),
          BUTTERFLY(
              "255,255,0,255,255\\n" //
                  + "255,255,255,255,255\\n"
                  + "0,0,255,0,0\\n"
                  + "255,255,255,255,255\\n"
                  + "255,255,0,255,255\\n" ),
          STICKFIGURE(
              "0,0,255,0,0\\n" //
                  + "255,255,255,255,255\\n"
                  + "0,0,255,0,0\\n"
                  + "0,255,0,255,0\\n"
                  + "255,0,0,0,255\\n" ),
          GHOST(
              "255,255,255,255,255\\n" //
                  + "255,0,255,0,255\\n"
                  + "255,255,255,255,255\\n"
                  + "255,255,255,255,255\\n"
                  + "255,0,255,0,255\\n" ),
          SWORD(
              "0,0,255,0,0\\n" //
                  + "0,0,255,0,0\\n"
                  + "0,0,255,0,0\\n"
                  + "0,255,255,255,0\\n"
                  + "0,0,255,0,0\\n" ),
          GIRAFFE(
              "255,255,0,0,0\\n" //
                  + "0,255,0,0,0\\n"
                  + "0,255,0,0,0\\n"
                  + "0,255,255,255,0\\n"
                  + "0,255,0,255,0\\n" ),
          SKULL(
              "0,255,255,255,0\\n" //
                  + "255,0,255,0,255\\n"
                  + "255,255,255,255,255\\n"
                  + "0,255,255,255,0\\n"
                  + "0,255,255,255,0\\n" ),
          UMBRELLA(
              "0,255,255,255,0\\n" //
                  + "255,255,255,255,255\\n"
                  + "0,0,255,0,0\\n"
                  + "255,0,255,0,0\\n"
                  + "0,255,255,0,0\\n" ),
        SNAKE(
            "255,255,0,0,0\\n" //
                + "255,255,0,255,255\\n"
                + "0,255,0,255,0\\n"
                + "0,255,255,255,0\\n"
                + "0,0,0,0,0\\n" );

        private final String[] values;
        private final String imageString;

        PredefinedImageNames(String imageString, String... values) {
            this.values = values;
            this.imageString = imageString;
        }

        public String getImageString() {
            return this.imageString;
        }

        /**
         * get predifined image from {@link PredefinedImageNames} from string parameter. It is possible for one image to have multiple string mappings. Throws
         * exception if the constant does not exists.
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
}
