package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoBasic(name = "METHOD_STMT", category = "STMT", blocklyNames = {})
public final class MethodStmt extends Stmt {
    public final Method method;

    public MethodStmt(Method method) {
        super(method.getProperty());
        Assert.isTrue(method != null && method.isReadOnly());
        this.method = method;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "MethodStmt [" + this.method + "]";
    }

    @Override
    public Block ast2xml() {
        return this.method.ast2xml();
    }
}
