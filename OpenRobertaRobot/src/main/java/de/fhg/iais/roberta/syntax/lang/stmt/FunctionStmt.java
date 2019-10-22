package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * Wraps subclasses of the class {@link Function} so they can be used as {@link Stmt} in statements.
 */
public class FunctionStmt<V> extends Stmt<V> {
    private final Function<V> function;

    private FunctionStmt(Function<V> function) {
        super(BlockTypeContainer.getByName("SENSOR_STMT"), function.getProperty(), function.getComment());
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitFunctionStmt(this);

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
