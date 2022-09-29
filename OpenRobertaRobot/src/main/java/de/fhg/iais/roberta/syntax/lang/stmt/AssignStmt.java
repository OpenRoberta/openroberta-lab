package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "ASSIGN_STMT", category = "STMT", blocklyNames = {"variables_set"})
public final class AssignStmt extends Stmt {
    public final Var name;
    public final Expr expr;

    public AssignStmt(BlocklyProperties properties, Var name, Expr expr) {
        super(properties);
        Assert.isTrue(name != null && expr != null && name.isReadOnly() && expr.isReadOnly());
        this.name = name;
        this.expr = expr;
        setReadOnly();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNewLine(sb, 0, null);
        sb.append(this.name).append(" := ").append(this.expr).append("\n");
        return sb.toString();
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase p = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.CAPTURED_TYPE));
        Expr exprr = Jaxb2Ast.convertPhraseToExpr(p);
        return new AssignStmt(Jaxb2Ast.extractBlocklyProperties(block), (Var) Jaxb2Ast.extractVar(block), Jaxb2Ast.convertPhraseToExpr(exprr));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        String varType = this.name.getVarType().getBlocklyName();

        Mutation mutation = new Mutation();
        mutation.setDatatype(varType);
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VAR, this.name.name);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.expr);

        return jaxbDestination;
    }

}
