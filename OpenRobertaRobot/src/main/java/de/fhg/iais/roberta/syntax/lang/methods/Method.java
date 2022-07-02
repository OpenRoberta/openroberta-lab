package de.fhg.iais.roberta.syntax.lang.methods;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

/**
 * the top class of all method classes used to represent the AST (abstract syntax tree) of a program. After construction an AST should be immutable. The logic
 * to achieve that is in the {@link Phrase} class. There are two ways for a client to find out which kind a {@link #Method}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Method<V> extends Phrase<V> {
    protected String methodName;
    protected ExprList<V> parameters;
    protected BlocklyType returnType;

    public Method(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
    }

    public String getMethodName() {
        return this.methodName;
    }

    public ExprList<V> getParameters() {
        return this.parameters;
    }

    public BlocklyType getReturnType() {
        return this.returnType;
    }

}
