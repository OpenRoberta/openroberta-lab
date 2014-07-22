package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_display_text</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code showing a text message on the screen of the brick.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, Expr, Expr)}.<br>
 * <br>
 * The client must provide the message and x and y coordinates.
 */
public class ShowTextAction extends Action {
    private final Expr msg;
    private final Expr x;
    private final Expr y;

    private ShowTextAction(Expr msg, Expr column, Expr row) {
        super(Phrase.Kind.ShowTextAction);
        Assert.isTrue(msg != null && column != null && row != null);
        this.msg = msg;
        this.x = column;
        this.y = row;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ShowTextAction}. This instance is read only and can not be modified.
     * 
     * @param msg that will be printed on the display of the brick,
     * @param x position where the message will start
     * @param y postition where the message will start
     * @return read only object of class {@link ShowTextAction}.
     */
    public static ShowTextAction make(Expr msg, Expr x, Expr y) {
        return new ShowTextAction(msg, x, y);
    }

    /**
     * @return the message.
     */
    public Expr getMsg() {
        return this.msg;
    }

    /**
     * @return position x of the picture on the display.
     */
    public Expr getX() {
        return this.x;
    }

    /**
     * @return position y of the picture on the display.
     */
    public Expr getY() {
        return this.y;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {

    }

    @Override
    public String toString() {
        return "ShowTextAction [" + this.msg + ", " + this.x + ", " + this.y + "]";
    }

}
