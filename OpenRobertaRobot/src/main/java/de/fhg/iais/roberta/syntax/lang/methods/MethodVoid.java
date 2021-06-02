package de.fhg.iais.roberta.syntax.lang.methods;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robProcedures_defnoreturn</b> block from Blockly into the AST (abstract syntax tree). Object from this class is used to create a
 * method with no return<br/>
 */
public class MethodVoid<V> extends Method<V> {
    private final StmtList<V> body;

    private MethodVoid(String methodName, ExprList<V> parameters, StmtList<V> body, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("METHOD_VOID"), properties, comment);
        Assert.isTrue(!methodName.equals("") && parameters.isReadOnly() && body.isReadOnly());
        this.methodName = methodName;
        this.parameters = parameters;
        this.returnType = BlocklyType.VOID;
        this.body = body;
        setReadOnly();
    }

    /**
     * creates instance of {@link MethodVoid}. This instance is read only and cannot be modified.
     *
     * @param methodName
     * @param parameters
     * @param body of the method
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MethodVoid}
     */
    public static <V> MethodVoid<V> make(
        String methodName,
        ExprList<V> parameters,
        StmtList<V> body,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new MethodVoid<V>(methodName, parameters, body, properties, comment);
    }

    /**
     * @return the body
     */
    public StmtList<V> getBody() {
        return this.body;
    }

    @Override
    public String toString() {
        return "MethodVoid [" + this.methodName + ", " + this.parameters + ", " + this.body + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String name = Jaxb2Ast.extractField(fields, BlocklyConstants.NAME);

        List<Statement> statements = Jaxb2Ast.extractStatements(block, (short) 2);
        ExprList<V> exprList = helper.statementsToMethodParameterDeclaration(statements, BlocklyConstants.ST);
        StmtList<V> statement = helper.extractStatement(statements, BlocklyConstants.STACK);

        return MethodVoid.make(name, exprList, statement, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        boolean declare = !this.parameters.get().isEmpty();
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDeclare(declare);
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.NAME, this.methodName);
        Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.ST, this.parameters);
        Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.STACK, this.body);
        return jaxbDestination;
    }

}
