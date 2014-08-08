package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.Action;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Action} so they can be used as {@link Expr} in expressions.
 */
public final class ActionExpr extends Expr {
    private final Action action;

    private ActionExpr(Action action) {
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
    public static ActionExpr make(Action action) {
        return new ActionExpr(action);
    }

    /**
     * @return action that is wrapped in the expression
     */
    public Action getAction() {
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
    public void accept(Visitor visitor) {
        visitor.visit(this);

    }
}
