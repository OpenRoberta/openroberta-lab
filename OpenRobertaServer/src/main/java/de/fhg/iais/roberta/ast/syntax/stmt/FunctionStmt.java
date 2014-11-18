package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.functions.Function;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Function} so they can be used as {@link Stmt} in statements.
 */
public class FunctionStmt<V> extends Stmt<V> {
    private final Function<V> function;

    private FunctionStmt(Function<V> function) {
        super(Phrase.Kind.SENSOR_STMT, null, null);
        Assert.isTrue(function != null && function.isReadOnly());
        this.function = function;
        setReadOnly();
    }

    /**
     * Create object of the class {@link SensorStmt}.
     *
     * @param function that we want to wrap
     * @return statement with wrapped function inside
     */
    public static <V> FunctionStmt<V> make(Function<V> function) {
        return new FunctionStmt<V>(function);
    }

    /**
     * @return function that is wrapped in the statement
     */
    public Function<V> getFunction() {
        return this.function;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitFunctionStmt(this);

    }

    @Override
    public String toString() {
        return "FunctionStmt [" + this.function + "]";
    }

    @Override
    public Block astToBlock() {
        return getFunction().astToBlock();
    }
}
