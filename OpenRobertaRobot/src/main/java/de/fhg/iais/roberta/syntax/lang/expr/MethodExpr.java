package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * Wraps subclasses of the class {@link Method} so they can be used as {@link Expr} in expressions.
 */
@NepoBasic(name = "METHOD_EXPR", category = "EXPR", blocklyNames = {})
public final class MethodExpr extends Expr {
    public final Method method;

    public MethodExpr(Method method) {
        super(method.getProperty());
        Assert.isTrue(method.isReadOnly());
        this.method = method;
        setReadOnly();
    }

    /**
     * @return method that is wrapped in the expression
     */
    public Method getMethod() {
        return this.method;
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
        return "MethodExpr [" + this.method + "]";
    }

    @Override
    public Block ast2xml() {
        Phrase p = getMethod();
        return p.ast2xml();
    }
}
