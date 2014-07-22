package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.Action;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Action} so they can be used as {@link Stmt} in statements.
 */
public class ActionStmt extends Stmt {
    private final Action akt;

    private ActionStmt(Action akt) {
        super(Phrase.Kind.AktionStmt);
        Assert.isTrue(akt.isReadOnly());
        this.akt = akt;
        setReadOnly();
    }

    /**
     * Create object of the class {@link AssignStmt}.
     * 
     * @param action that we want to wrap
     * @return statement with wrapped action inside
     */
    public static ActionStmt make(Action akt) {
        return new ActionStmt(akt);
    }

    /**
     * @return action that is wrapped in the statement
     */
    public Action getAkt() {
        return this.akt;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        appendNewLine(sb, indentation, null);
        sb.append("SensorStmt ").append(this.akt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nAktionStmt [" + this.akt + "]");
        return sb.toString();
    }
}
