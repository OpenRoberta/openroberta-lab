package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * Wraps subclasses of the class {@link Action} so they can be used as {@link Expr} in expressions.
 */
@NepoBasic(category = "EXPR", name = "ACTION_EXPR", blocklyNames = {})
public final class ActionExpr extends Expr {
    public final Action action;

    public ActionExpr(Action action) {
        super(action.getProperty());
        Assert.isTrue(action.isReadOnly());
        this.action = action;
        setReadOnly();
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
    public Block ast2xml() {
        return this.action.ast2xml();
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.CAPTURED_TYPE;
    }
}
