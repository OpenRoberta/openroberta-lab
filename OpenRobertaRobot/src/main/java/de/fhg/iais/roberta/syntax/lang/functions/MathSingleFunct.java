package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

@NepoBasic(name = "MATH_SINGLE_FUNCT", category = "FUNCTION", blocklyNames = {"math_trig", "math_single", "math_round"})
public final class MathSingleFunct extends Function {
    public final FunctionNames functName;
    public final List<Expr> param;

    public MathSingleFunct(FunctionNames name, List<Expr> param, BlocklyProperties properties) {
        super(properties);
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
        return "MathSingleFunct [" + this.functName + ", " + this.param + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        if ( Jaxb2Ast.getOperation(block, BlocklyConstants.OP).equals(BlocklyConstants.NEG) ) {
            return helper.blockToUnaryExpr(block, new ExprParam(BlocklyConstants.NUM, BlocklyType.NUMBER_INT), BlocklyConstants.OP);
        }
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.NUM, BlocklyType.NUMBER_INT));
        String op = Jaxb2Ast.getOperation(block, BlocklyConstants.OP);
        List<Expr> params = helper.extractExprParameters(block, exprParams);
        return new MathSingleFunct(FunctionNames.get(op), params, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.OP, this.functName.name());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.NUM, this.param.get(0));
        return jaxbDestination;
    }

}
