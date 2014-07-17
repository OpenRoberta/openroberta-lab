package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * @author kcvejoski
 */
public class ShowPictureAction extends Action {
    private final String pic;
    private final Expr x;
    private final Expr y;

    private ShowPictureAction(String msg, Expr column, Expr row) {
        super(Phrase.Kind.ShowPictureAction);
        Assert.isTrue(msg != null && column != null && row != null);
        this.pic = msg;
        this.x = column;
        this.y = row;
        setReadOnly();
    }

    public static ShowPictureAction make(String msg, Expr column, Expr row) {
        return new ShowPictureAction(msg, column, row);
    }

    public String getPicture() {
        return this.pic;
    }

    public Expr getColumn() {
        return this.x;
    }

    public Expr getRow() {
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
