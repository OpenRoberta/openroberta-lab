package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Function} so they can be used as {@link Stmt} in statements.
 */
@NepoBasic(name = "FUNCTION_STMT", category = "STMT", blocklyNames = {})
public final class FunctionStmt extends Stmt {
    public final Function function;

    public FunctionStmt(Function function) {
        super(function.getProperty());
        Assert.isTrue(function != null && function.isReadOnly());
        this.function = function;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "FunctionStmt [" + this.function + "]";
    }

    @Override
    public Block ast2xml() {
        return this.function.ast2xml();
    }
}
