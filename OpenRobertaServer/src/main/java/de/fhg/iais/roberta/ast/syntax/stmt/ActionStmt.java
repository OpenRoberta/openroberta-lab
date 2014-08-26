package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.Action;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Action} so they can be used as {@link Stmt} in statements.
 */
public class ActionStmt<V> extends Stmt<V> {
    private final Action<V> action;

    private ActionStmt(Action<V> action) {
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
    public static <V> ActionStmt<V> make(Action<V> action) {
        return new ActionStmt<V>(action);
    }

    /**
     * @return action that is wrapped in the statement
     */
    public Action<V> getAction() {
        return this.action;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nAktionStmt [" + this.action + "]");
        return sb.toString();
    }

    @Override
    protected V accept(Visitor<V> visitor) {
        return visitor.visitActionStmt(this);
    }
}
