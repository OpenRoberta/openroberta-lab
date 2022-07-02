package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Function} so they can be used as {@link Stmt} in statements.
 */
@NepoBasic(name = "FUNCTION_STMT", category = "STMT", blocklyNames = {})
public final class FunctionStmt<V> extends Stmt<V> {
    public final Function<V> function;

    public FunctionStmt(Function<V> function) {
        super(function.getProperty(), function.getComment());
        Assert.isTrue(function != null && function.isReadOnly());
        this.function = function;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "FunctionStmt [" + this.function + "]";
    }

    @Override
    public Block astToBlock() {
        return this.function.astToBlock();
    }
}
