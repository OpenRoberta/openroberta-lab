package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * Wraps subclasses of the class {@link Method} so they can be used as {@link Expr} in expressions.
 */
public class MethodExpr<V> extends Expr<V> {
    private final Method<V> method;

    private MethodExpr(Method<V> method) {
        super(BlockTypeContainer.getByName("METHOD_EXPR"), method.getProperty(), method.getComment());
        Assert.isTrue(method.isReadOnly());
        this.method = method;
        setReadOnly();
    }

    /**
     * Create object of the class {@link MethodExpr}.
     *
     * @param method that we want to wrap,
     * @return expression with wrapped function inside
     */
    public static <V> MethodExpr<V> make(Method<V> method) {
        return new MethodExpr<V>(method);
    }

    /**
     * @return method that is wrapped in the expression
     */
    public Method<V> getMethod() {
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitMethodExpr(this);
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
    public Block astToBlock() {
        Phrase<V> p = getMethod();
        return p.astToBlock();
    }
}
