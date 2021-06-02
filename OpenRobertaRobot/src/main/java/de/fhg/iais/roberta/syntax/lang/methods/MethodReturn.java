package de.fhg.iais.roberta.syntax.lang.methods;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robProcedures_defreturn</b> block from Blockly into the AST (abstract syntax tree). Object from this class is used to create a
 * method with return value<br/>
 */
public class MethodReturn<V> extends Method<V> {
    private final StmtList<V> body;
    private final Expr<V> returnValue;

    private MethodReturn(
        String methodName,
        ExprList<V> parameters,
        StmtList<V> body,
        BlocklyType returnType,
        Expr<V> returnValue,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("METHOD_RETURN"), properties, comment);
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
     * @return the body
     */
    public StmtList<V> getBody() {
        return this.body;
    }

    /**
     * @return the returnValue
     */
    public Expr<V> getReturnValue() {
        return this.returnValue;
    }

    @Override
    public String toString() {
        return "MethodReturn ["
            + this.getMethodName()
            + ", "
            + this.getParameters()
            + ", "
            + this.body
            + ", "
            + this.returnType
            + ", "
            + this.returnValue
            + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String name = Jaxb2Ast.extractField(fields, BlocklyConstants.NAME);

        List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
        ArrayList<Value> values = new ArrayList<Value>();
        ArrayList<Statement> statements = new ArrayList<Statement>();
        Jaxb2Ast.convertStmtValList(values, statements, valAndStmt);
        ExprList<V> exprList = helper.statementsToMethodParameterDeclaration(statements, BlocklyConstants.ST);
        StmtList<V> statement = helper.extractStatement(statements, BlocklyConstants.STACK);
        Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.RETURN, BlocklyType.NULL));

        return MethodReturn
            .make(
                name,
                exprList,
                statement,
                BlocklyType.get(Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE)),
                Jaxb2Ast.convertPhraseToExpr(expr),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        boolean declare = !this.getParameters().get().isEmpty();
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDeclare(declare);
        mutation.setReturnType(this.returnType.getBlocklyName());
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.NAME, this.getMethodName());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.returnType.getBlocklyName());
        Repetitions repetition = new Repetitions();
        Ast2Jaxb.addStatement(repetition, BlocklyConstants.ST, this.getParameters());
        Ast2Jaxb.addStatement(repetition, BlocklyConstants.STACK, this.body);
        Ast2Jaxb.addValue(repetition, BlocklyConstants.RETURN, getReturnValue());
        jaxbDestination.setRepetitions(repetition);
        return jaxbDestination;
    }

}
