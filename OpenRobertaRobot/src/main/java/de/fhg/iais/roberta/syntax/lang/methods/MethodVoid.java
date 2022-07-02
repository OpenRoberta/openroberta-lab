package de.fhg.iais.roberta.syntax.lang.methods;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "METHOD_VOID", category = "METHOD", blocklyNames = {"robProcedures_defnoreturn"})
public final class MethodVoid<V> extends Method<V> {
    public final StmtList<V> body;

    public MethodVoid(String methodName, ExprList<V> parameters, StmtList<V> body, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        Assert.isTrue(!methodName.equals("") && parameters.isReadOnly() && body.isReadOnly());
        this.methodName = methodName;
        this.parameters = parameters;
        this.returnType = BlocklyType.VOID;
        this.body = body;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "MethodVoid [" + this.methodName + ", " + this.parameters + ", " + this.body + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String name = Jaxb2Ast.extractField(fields, BlocklyConstants.NAME);

        List<Statement> statements = Jaxb2Ast.extractStatements(block, (short) 2);
        ExprList<V> exprList = helper.statementsToMethodParameterDeclaration(statements, BlocklyConstants.ST);
        StmtList<V> statement = helper.extractStatement(statements, BlocklyConstants.STACK);

        return new MethodVoid<V>(name, exprList, statement, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
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
