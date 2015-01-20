package de.fhg.iais.roberta.ast.syntax.methods;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robProcedures_defreturn</b> block from Blockly
 * into the AST (abstract syntax tree). Object from this class is used to create a method with return value<br/>
 */
public class MethodReturn<V> extends Method<V> {
    private final String methodName;
    private final ExprList<V> parameters;
    private final StmtList<V> body;
    private final Var<V> returnType;
    private final Expr<V> returnValue;

    private MethodReturn(
        String methodName,
        ExprList<V> parameters,
        StmtList<V> body,
        Var<V> returnType,
        Expr<V> returnValue,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(Phrase.Kind.VAR, properties, comment);
        Assert.isTrue(!methodName.equals("") && parameters.isReadOnly() && body.isReadOnly() && returnType.isReadOnly() && returnValue.isReadOnly());
        this.methodName = methodName;
        this.parameters = parameters;
        this.body = body;
        this.returnType = returnType;
        this.returnValue = returnValue;
        setReadOnly();
    }

    /**
     * creates instance of {@link MethodReturn}. This instance is read only and cannot be modified.
     *
     * @param methodName
     * @param parameters
     * @param body of the method
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @param return_ type of the method
     * @return read only object of class {@link MethodReturn}
     */
    public static <V> MethodReturn<V> make(
        String methodName,
        ExprList<V> parameters,
        StmtList<V> body,
        Var<V> returnType,
        Expr<V> returnValue,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new MethodReturn<V>(methodName, parameters, body, returnType, returnValue, properties, comment);
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * @return the parameters
     */
    public ExprList<V> getParameters() {
        return this.parameters;
    }

    /**
     * @return the body
     */
    public StmtList<V> getBody() {
        return this.body;
    }

    /**
     * @return the return_
     */
    public Expr<V> getReturnType() {
        return this.returnType;
    }

    /**
     * @return the returnValue
     */
    public Expr<V> getReturnValue() {
        return this.returnValue;
    }

    @Override
    public String toString() {
        return "MethodReturn [" + this.methodName + ", " + this.parameters + ", " + this.body + ", " + this.returnType + ", " + this.returnValue + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMethodReturn(this);
    }

    @Override
    public Block astToBlock() {
        boolean declare = this.parameters.get().size() == 0 ? false : true;
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDeclare(declare);
        mutation.setReturnType(this.returnType.getTypeVar().getBlocklyName());
        jaxbDestination.setMutation(mutation);
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.NAME, this.methodName);
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.TYPE, this.returnType.getTypeVar().getBlocklyName());
        Repetitions repetition = new Repetitions();
        AstJaxbTransformerHelper.addStatement(repetition, BlocklyConstants.ST, this.parameters);
        AstJaxbTransformerHelper.addStatement(repetition, BlocklyConstants.STACK, this.body);
        AstJaxbTransformerHelper.addValue(repetition, BlocklyConstants.RETURN, getReturnValue());
        jaxbDestination.setRepetitions(repetition);
        return jaxbDestination;
    }

}
