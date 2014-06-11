package de.fhg.iais.roberta.ast.syntax.aktion;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class ShowAktion extends Aktion {
    private final String msg;
    private final int column;
    private final int row;

    private ShowAktion(String msg, int column, int row) {
        super(Phrase.Kind.ShowAktion);
        Assert.isTrue(msg != null && column > 0 && row > 0);
        this.msg = msg;
        this.column = column;
        this.row = row;
        setReadOnly();
    }

    public ShowAktion make(String msg, int column, int row) {
        return new ShowAktion(msg, column, row);
    }

    public String getMsg() {
        return this.msg;
    }

    public int getColumn() {
        return this.column;
    }

    public int getRow() {
        return this.row;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {

    }

    @Override
    public String toString() {
        return "ShowAktion [" + this.msg + ", " + this.column + ", " + this.row + "]";
    }

}
