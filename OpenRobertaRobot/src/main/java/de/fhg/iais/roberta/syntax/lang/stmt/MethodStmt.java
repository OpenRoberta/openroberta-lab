package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoBasic(name = "METHOD_STMT", category = "STMT", blocklyNames = {})
public final class MethodStmt<V> extends Stmt<V> {
    public final Method<V> method;

    public MethodStmt(Method<V> method) {
        super(method.getProperty(), method.getComment());
        Assert.isTrue(method != null && method.isReadOnly());
        this.method = method;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "MethodStmt [" + this.method + "]";
    }

    @Override
    public Block astToBlock() {
        return this.method.astToBlock();
    }
}
