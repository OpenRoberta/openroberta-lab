package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_display_text</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code showing a text message on the screen of the brick.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, Expr, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * <br>
 * The client must provide the message and x and y coordinates.
 */
public class ShowTextAction<V> extends Action<V> {
    private final Expr<V> msg;
    private final Expr<V> x;
    private final Expr<V> y;

    private ShowTextAction(Expr<V> msg, Expr<V> column, Expr<V> row, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.SHOW_TEXT_ACTION, properties, comment);
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
     * @param x position where the message will start,
     * @param y position where the message will start,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ShowTextAction}.
     */
    public static <V> ShowTextAction<V> make(Expr<V> msg, Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ShowTextAction<V>(msg, x, y, properties, comment);
    }

    /**
     * @return the message.
     */
    public Expr<V> getMsg() {
        return this.msg;
    }

    /**
     * @return position x of the picture on the display.
     */
    public Expr<V> getX() {
        return this.x;
    }

    /**
     * @return position y of the picture on the display.
     */
    public Expr<V> getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "ShowTextAction [" + this.msg + ", " + this.x + ", " + this.y + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitShowTextAction(this);
    }

}
