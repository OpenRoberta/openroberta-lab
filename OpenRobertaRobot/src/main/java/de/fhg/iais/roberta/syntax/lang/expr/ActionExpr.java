package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * Wraps subclasses of the class {@link Action} so they can be used as {@link Expr} in expressions.
 */
public final class ActionExpr<V> extends Expr<V> {
    private final Action<V> action;

    private ActionExpr(Action<V> action) {
        super(BlockTypeContainer.getByName("ACTION_EXPR"), action.getProperty(), action.getComment());
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitActionExpr(this);
    }

    @Override
    public Block astToBlock() {
        return getAction().astToBlock();
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.CAPTURED_TYPE;
    }
}
