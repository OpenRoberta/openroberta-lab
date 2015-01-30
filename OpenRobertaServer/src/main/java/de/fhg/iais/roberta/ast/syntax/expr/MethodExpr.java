package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.methods.Method;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Method} so they can be used as {@link Expr} in expressions.
 */
public class MethodExpr<V> extends Expr<V> {
    private final Method<V> method;

    private MethodExpr(Method<V> method) {
        super(Phrase.Kind.METHOD_EXPR, null, null);
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMethodExpr(this);
    }

    @Override
    public String toString() {
        return "MethodExpr [" + this.method + "]";
    }

    @Override
    public Block astToBlock() {
        Phrase<?> p = ((MethodExpr<?>) this).getMethod();
        return p.astToBlock();
    }
}
