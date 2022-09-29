package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * {@link EmptyExpr} is used when in binary or unary expressions, expression is missing. When we create instances from this class we pass as parameter the type of
 * the value should have the missing expression.
 */
@NepoBasic(name = "EMPTY_EXPR", category = "EXPR", blocklyNames = {})
public final class EmptyExpr extends Expr {

    public final BlocklyType defVal;

    public EmptyExpr(BlocklyType defVal) {
        super(BlocklyProperties.make("EMPTY_EXPR", "1"));
        Assert.isTrue(defVal != null);
        this.defVal = defVal;
        setReadOnly();
    }

    /**
     * @return type of the value that the missing expression should have.
     */
    public BlocklyType getDefVal() {
        return this.defVal;
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
    public String toString() {
        return "EmptyExpr [defVal=" + this.defVal + "]";
    }

    @Override
    public Block ast2xml() {
        return null;
    }

}
