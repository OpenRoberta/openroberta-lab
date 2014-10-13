package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>robActions_display_picture</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code showing a picture on the screen of the brick.<br/>
 * <br/>
 * The client must provide the name of the picture and x and y coordinates.
 */
public class ShowPictureAction<V> extends Action<V> {
    private final Picture pic;
    private final Expr<V> x;
    private final Expr<V> y;

    private ShowPictureAction(Picture pic, Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.SHOW_PICTURE_ACTION, properties, comment);
        Assert.isTrue(pic != null && x != null && y != null);
        this.pic = pic;
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ShowPictureAction}. This instance is read only and can not be modified.
     *
     * @param pic that will be printed on the display of the brick,
     * @param x position where the picture will start,
     * @param y position where the picture will start,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ShowPictureAction}.
     */
    public static <V> ShowPictureAction<V> make(Picture pic, Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ShowPictureAction<V>(pic, x, y, properties, comment);
    }

    /**
     * @return name of the picture that
     */
    public Picture getPicture() {
        return this.pic;
    }

    /**
     * @return position x of the picture on the display.
     */
    public Expr<V> getX() {
        return this.x;
    }

    /**
     * @return position y of the picture on the display
     */
    public Expr<V> getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "ShowPictureAction [" + this.pic + ", " + this.x + ", " + this.y + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitShowPictureAction(this);
    }

    public static enum Picture {
        SMILEY1(), SMILEY2(), SMILEY3(), SMILEY4();

        private final String[] values;

        private Picture(String... values) {
            this.values = values;
        }

        /**
         * @return valid Java code name of the enumeration
         */
        public String getJavaCode() {
            return this.getClass().getSimpleName() + "." + this;
        }

        /**
         * get picture from {@link Picture} from string parameter. It is possible
         * for one mode to have multiple string mappings. Throws exception if
         * the mode does not exists.
         *
         * @param name of the picture
         * @return picture from the enum {@link Picture}
         */
        public static Picture get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid picture: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Picture mo : Picture.values() ) {
                if ( mo.toString().equals(sUpper) ) {
                    return mo;
                }
                for ( String value : mo.values ) {
                    if ( sUpper.equals(value) ) {
                        return mo;
                    }
                }
            }
            throw new DbcException("Invalid picture: " + s);
        }
    }
}
