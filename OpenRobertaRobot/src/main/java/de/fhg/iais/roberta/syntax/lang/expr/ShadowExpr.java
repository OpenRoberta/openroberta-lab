package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

public final class ShadowExpr extends Expr {
    public final Expr shadow;
    public final Expr block;

    public ShadowExpr(Expr shadow, Expr block) {
        super(BlocklyProperties.make("SHADOW_EXPR", "1"));
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
    public Block ast2xml() {
        return null;
    }

    @Override
    public String toString() {
        return "ShadowExpr [" + this.shadow + ", " + this.block + "]";
    }

}
