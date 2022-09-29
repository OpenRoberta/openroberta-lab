package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * Wraps subclasses of the class {@link Function} so they can be used as {@link Expr} in expressions.
 */
@NepoBasic(name = "FUNCTION_EXPR", category = "EXPR", blocklyNames = {})
public final class FunctionExpr extends Expr {
    public final Function function;

    public FunctionExpr(Function function) {
        super(function.getProperty());
        Assert.isTrue(function.isReadOnly());
        this.function = function;
        setReadOnly();
    }

    /**
     * @return function that is wrapped in the expression
     */
    public Function getFunction() {
        return this.function;
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
        return this.function.getReturnType();
    }

    @Override
    public String toString() {
        return "FunctionExpr [" + this.function + "]";
    }

    @Override
    public Block ast2xml() {
        Phrase p = this.getFunction();
        return p.ast2xml();
    }
}
