package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * Wraps subclasses of the class {@link Action} so they can be used as {@link Stmt} in statements.
 */
public class ActionStmt<V> extends Stmt<V> {
    private final Action<V> action;

    private ActionStmt(Action<V> action) {
        super(BlockTypeContainer.getByName("AKTION_STMT"), action.getProperty(), action.getComment());
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitActionStmt(this);
    }

    @Override
    public Block astToBlock() {
        return getAction().astToBlock();
    }
}
