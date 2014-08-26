package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.Action;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Action} so they can be used as {@link Expr} in expressions.
 */
public final class ActionExpr<V> extends Expr<V> {
    private final Action<V> action;

    private ActionExpr(Action<V> action) {
        super(Phrase.Kind.ACTION_EXPR);
        Assert.isTrue(action.isReadOnly());
        this.action = action;
        setReadOnly();
    }

    /**
     * Create object of the class {@link ActionExpr}.
     * 
     * @param action that we want to wrap
     * @return expression with wrapped action inside
     */
    public static <V> ActionExpr<V> make(Action<V> action) {
        return new ActionExpr<V>(action);
    }

    /**
     * @return action that is wrapped in the expression
     */
    public Action<V> getAction() {
        return this.action;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public String toString() {
        return "ActionExpr [" + this.action + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitActionExpr(this);
    }
}
