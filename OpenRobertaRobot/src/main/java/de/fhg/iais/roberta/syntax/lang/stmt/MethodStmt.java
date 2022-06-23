package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoBasic(name = "METHOD_STMT", category = "STMT", blocklyNames = {})
public final class MethodStmt<V> extends Stmt<V> {
    public final Method<V> method;

    private MethodStmt(Method<V> method) {
        super(method.getProperty(), method.getComment());
        Assert.isTrue(method != null && method.isReadOnly());
        this.method = method;
        setReadOnly();
    }

    /**
     * Create object of the class {@link MethodStmt}.
     *
     * @param method that we want to wrap
     * @return statement with wrapped method inside
     */
    public static <V> MethodStmt<V> make(Method<V> method) {
        return new MethodStmt<V>(method);
    }

    /**
     * @return the method
     */
    public Method<V> getMethod() {
        return this.method;
    }

    @Override
    public String toString() {
        return "MethodStmt [" + this.method + "]";
    }

    @Override
    public Block astToBlock() {
        return getMethod().astToBlock();
    }
}
