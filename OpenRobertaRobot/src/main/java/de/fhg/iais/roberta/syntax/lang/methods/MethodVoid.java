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
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "METHOD_VOID", category = "METHOD", blocklyNames = {"robProcedures_defnoreturn"})
public final class MethodVoid extends Method {
    public final StmtList body;

    public MethodVoid(String methodName, ExprList parameters, StmtList body, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(!methodName.equals("") && parameters.isReadOnly() && body.isReadOnly());
        this.methodName = Util.sanitizeProgramProperty(methodName);
        this.parameters = parameters;
        this.returnType = BlocklyType.VOID;
        this.body = body;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "MethodVoid [" + this.methodName + ", " + this.parameters + ", " + this.body + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String name = Jaxb2Ast.extractField(fields, BlocklyConstants.NAME);

        List<Statement> statements = Jaxb2Ast.extractStatements(block, (short) 2);
        ExprList exprList = helper.statementsToMethodParameterDeclaration(statements, BlocklyConstants.ST);
        StmtList statement = helper.extractStatement(statements, BlocklyConstants.STACK);

        return new MethodVoid(name, exprList, statement, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
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
