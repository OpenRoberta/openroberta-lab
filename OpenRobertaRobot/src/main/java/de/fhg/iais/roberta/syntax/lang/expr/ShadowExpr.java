package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

public class ShadowExpr<V> extends Expr<V> {
    public final Expr<V> shadow;
    public final Expr<V> block;

    public ShadowExpr(Expr<V> shadow, Expr<V> block) {
        super(BlocklyBlockProperties.make("SHADOW_EXPR", "1"), null);
        Assert.isTrue(shadow != null);
        this.shadow = shadow;
        this.block = block;
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
    public BlocklyType getVarType() {
        return BlocklyType.NOTHING;
    }

    @Override
    public Block astToBlock() {
        return null;
    }

    @Override
    public String toString() {
        return "ShadowExpr [" + this.shadow + ", " + this.block + "]";
    }

}
