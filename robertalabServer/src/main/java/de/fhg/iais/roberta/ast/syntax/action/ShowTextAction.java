package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * @author kcvejoski
 */
public class ShowTextAction extends Action {
    private final Expr msg;
    private final Expr column;
    private final Expr row;

    private ShowTextAction(Expr msg, Expr column, Expr row) {
        super(Phrase.Kind.ShowTextAction);
        Assert.isTrue(msg != null && column != null && row != null);
        this.msg = msg;
        this.column = column;
        this.row = row;
        setReadOnly();
    }

    public static ShowTextAction make(Expr msg, Expr column, Expr row) {
        return new ShowTextAction(msg, column, row);
    }

    public Expr getMsg() {
        return this.msg;
    }

    public Expr getColumn() {
        return this.column;
    }

    public Expr getRow() {
        return this.row;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {

    }

    @Override
    public String toString() {
        return "ShowTextAction [" + this.msg + ", " + this.column + ", " + this.row + "]";
    }

}
