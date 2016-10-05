package de.fhg.iais.roberta.syntax.methods;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robProcedures_defreturn</b> block from Blockly
 * into the AST (abstract syntax tree). Object from this class is used to create a method with return value<br/>
 */
public class MethodReturn<V> extends Method<V> {
    private final String methodName;
    private final ExprList<V> parameters;
    private final StmtList<V> body;
    private final BlocklyType returnType;
    private final Expr<V> returnValue;

    private MethodReturn(
        String methodName,
        ExprList<V> parameters,
        StmtList<V> body,
        BlocklyType returnType,
        Expr<V> returnValue,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("METHOD_RETURN"),properties, comment);
        Assert.isTrue(!methodName.equals("") && parameters.isReadOnly() && body.isReadOnly() && returnValue.isReadOnly());
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
        BlocklyType returnType,
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
    public BlocklyType getReturnType() {
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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String name = helper.extractField(fields, BlocklyConstants.NAME);

        List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
        ArrayList<Value> values = new ArrayList<Value>();
        ArrayList<Statement> statements = new ArrayList<Statement>();
        helper.convertStmtValList(values, statements, valAndStmt);
        ExprList<V> exprList = helper.statementsToExprs(statements, BlocklyConstants.ST);
        StmtList<V> statement = helper.extractStatement(statements, BlocklyConstants.STACK);
        Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.RETURN, NullConst.class));

        return MethodReturn.make(
            name,
            exprList,
            statement,
            BlocklyType.get(helper.extractField(fields, BlocklyConstants.TYPE)),
            helper.convertPhraseToExpr(expr),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        boolean declare = this.parameters.get().size() == 0 ? false : true;
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDeclare(declare);
        mutation.setReturnType(this.returnType.getBlocklyName());
        jaxbDestination.setMutation(mutation);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.NAME, this.methodName);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.TYPE, this.returnType.getBlocklyName());
        Repetitions repetition = new Repetitions();
        JaxbTransformerHelper.addStatement(repetition, BlocklyConstants.ST, this.parameters);
        JaxbTransformerHelper.addStatement(repetition, BlocklyConstants.STACK, this.body);
        JaxbTransformerHelper.addValue(repetition, BlocklyConstants.RETURN, getReturnValue());
        jaxbDestination.setRepetitions(repetition);
        return jaxbDestination;
    }

}
