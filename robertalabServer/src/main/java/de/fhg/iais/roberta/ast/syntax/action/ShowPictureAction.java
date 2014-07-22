package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_display_picture</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code showing a picture on the screen of the brick.<br/>
 * <br/>
 * The client must provide the name of the picture and x and y coordinates.
 */
public class ShowPictureAction extends Action {
    private final String pic;
    private final Expr x;
    private final Expr y;

    private ShowPictureAction(String pic, Expr x, Expr y) {
        super(Phrase.Kind.ShowPictureAction);
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
     * @param x position where the picture will start
     * @param y postition where the picture will start
     * @return read only object of class {@link ShowPictureAction}.
     */
    public static ShowPictureAction make(String pic, Expr x, Expr y) {
        return new ShowPictureAction(pic, x, y);
    }

    /**
     * @return name of the picture that
     */
    public String getPicture() {
        return this.pic;
    }

    /**
     * @return position x of the picture on the display.
     */
    public Expr getX() {
        return this.x;
    }

    /**
     * @return position y of the picture on the display
     */
    public Expr getY() {
        return this.y;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {

    }

    @Override
    public String toString() {
        return "ShowPictureAction [" + this.pic + ", " + this.x + ", " + this.y + "]";
    }

}
