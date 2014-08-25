package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.Action;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Action} so they can be used as {@link Stmt} in statements.
 */
public class ActionStmt extends Stmt {
    private final Action action;

    private ActionStmt(Action action) {
        super(Phrase.Kind.AKTION_STMT);
        Assert.isTrue(action.isReadOnly());
        this.action = action;
        setReadOnly();
    }

    /**
     * Create object of the class {@link AssignStmt}.
     * 
     * @param action that we want to wrap
     * @return statement with wrapped action inside
     */
    public static ActionStmt make(Action action) {
        return new ActionStmt(action);
    }

    /**
     * @return action that is wrapped in the statement
     */
    public Action getAction() {
        return this.action;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nAktionStmt [" + this.action + "]");
        return sb.toString();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitActionStmt(this);
    }
}
