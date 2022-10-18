package de.fhg.iais.roberta.syntax.spike;

import java.util.Locale;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "EXPR", blocklyNames = {"image_get_image"}, name = "PREDEFINED_IMAGE")
public final class PredefinedImage extends Expr {
    @NepoField(name = BlocklyConstants.IMAGE)
    public final String imageName;

    public PredefinedImage(BlocklyProperties properties, String imageName) {
        super(properties);
        Assert.notNull(imageName);
        this.imageName = imageName;
        setReadOnly();
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
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
    public BlocklyType getVarType() {
        return BlocklyType.STRING;
    }

    public enum PredefinedImageNames {
        HEART("09090:99999:99999:09990:00900"),
        HEART_SMALL("00000:09090:09990:00900:00000"),
        HAPPY("00000:09090:00000:90009:09990"),
        SMILE("00000:00000:00000:90009:09990"),
        SAD("00000:09090:00000:09990:90009"),
        CONFUSED("00000:09090:00000:09090:90909"),
        ANGRY("90009:09090:00000:99999:90909"),
        ASLEEP("00000:99099:00000:09990:00000"),
        SURPRISED("09090:00000:00900:09090:00900"),
        SILLY("90009:00000:99999:00909:00999"),
        FABULOUS("99999:99099:00000:09090:09990"),
        MEH("09090:00000:00090:00900:09000"),
        YES("00000:00009:00090:90900:09000"),
        NO("90009:09090:00900:09090:90009"),
        TRIANGLE("00000:00900:09090:99999:00000"),
        TRIANGLE_LEFT("90000:99000:90900:90090:99999"),
        CHESSBOARD("09090:90909:09090:90909:09090"),
        DIAMOND("00900:09090:90009:09090:00900"),
        DIAMOND_SMALL("00000:00900:09090:00900:00000"),
        SQUARE("99999:90009:90009:90009:99999"),
        SQUARE_SMALL("00000:09990:09090:09990:00000"),
        RABBIT("90900:90900:99990:99090:99990"),
        COW("90009:90009:99999:09990:00900"),
        MUSIC_CROTCHET("00900:00900:00900:99900:99900"),
        MUSIC_QUAVER("00900:00990:00909:99900:99900"),
        MUSIC_QUAVERS("09999:09009:09009:99099:99099"),
        PITCHFORK("90909:90909:99999:00900:00900"),
        XMAS("00900:09990:00900:09990:99999"),
        PACMAN("09999:99090:99900:99990:09999"),
        TARGET("00900:09990:99099:09990:00900"),
        TSHIRT("99099:99999:09990:09990:09990"),
        ROLLERSKATE("00099:00099:99999:99999:09090"),
        DUCK("09900:99900:09999:09990:00000"),
        HOUSE("00900:09990:99999:09990:09090"),
        TORTOISE("00000:09990:99999:09090:00000"),
        BUTTERFLY("99099:99999:00900:99999:99099"),
        STICKFIGURE("00900:99999:00900:09090:90009"),
        GHOST("99999:90909:99999:99999:90909"),
        SWORD("00900:00900:00900:09990:00900"),
        GIRAFFE("99000:09000:09000:09990:09090"),
        SKULL("09990:90909:99999:09990:09990"),
        UMBRELLA("09990:99999:00900:90900:09900"),
        SNAKE("99000:99099:09090:09990:00000"),
        ;

        public final String[] values;
        public final String imageString;

        PredefinedImageNames(String imageString, String... values) {
            this.values = values;
            this.imageString = imageString;
        }

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

        public String getImageString() {
            return this.imageString;
        }
    }
}
