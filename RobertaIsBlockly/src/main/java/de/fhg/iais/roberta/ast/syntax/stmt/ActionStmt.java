package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.Action;
import de.fhg.iais.roberta.dbc.Assert;

public class ActionStmt extends Stmt {
    private final Action akt;

    private ActionStmt(Action akt) {
        super(Phrase.Kind.AktionStmt);
        Assert.isTrue(akt.isReadOnly());
        this.akt = akt;
        setReadOnly();
    }

    public static ActionStmt make(Action akt) {
        return new ActionStmt(akt);
    }

    public Action getAkt() {
        return this.akt;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        appendNewLine(sb, indentation, null);
        sb.append("SensorStmt ").append(this.akt);
    }

    @Override
    public String toString() {
        return "AktionStmt [" + this.akt + "]";
    }

}
