package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.functions.Function;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Function} so they can be used as {@link Expr} in expressions.
 */
public class FunctionExpr<V> extends Expr<V> {
    private final Function<V> function;

    private FunctionExpr(Function<V> function) {
        super(Phrase.Kind.FUNCTION_EXPR, null, null);
        Assert.isTrue(function.isReadOnly());
        this.function = function;
        setReadOnly();
    }

    /**
     * Create object of the class {@link FunctionExpr}.
     *
     * @param function that we want to wrap,
     * @return expression with wrapped function inside
     */
    public static <V> FunctionExpr<V> make(Function<V> function) {
        return new FunctionExpr<V>(function);
    }

    /**
     * @return function that is wrapped in the expression
     */
    public Function<V> getFunction() {
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
    protected V accept(AstVisitor<V> visitor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return "FunctionExpr [" + this.function + "]";
    }
}
