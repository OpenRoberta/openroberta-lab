package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

@NepoBasic(name = "MATH_ON_LIST_FUNCT", category = "FUNCTION", blocklyNames = {"math_on_list"})
public final class MathOnListFunct<V> extends Function<V> {
    public final FunctionNames functName;
    public final List<Expr<V>> param;

    public MathOnListFunct(FunctionNames name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        Assert.isTrue(name != null && param != null);
        this.functName = name;
        this.param = param;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return this.functName.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.functName.getAssoc();
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.NUMBER;
    }

    @Override
    public String toString() {
        return "MathOnListFunct [" + this.functName + ", " + this.param + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.LIST, BlocklyType.ARRAY));
        String op = Jaxb2Ast.getOperation(block, BlocklyConstants.OP);
        List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
        return new MathOnListFunct<V>(FunctionNames.get(op), params, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setOp(this.functName.name());
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.OP, this.functName.name());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.LIST, this.param.get(0));
        return jaxbDestination;
    }
}
