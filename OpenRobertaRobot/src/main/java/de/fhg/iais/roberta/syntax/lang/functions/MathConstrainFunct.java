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

@NepoBasic(name = "MATH_CONSTRAIN_FUNCT", category = "FUNCTION", blocklyNames = {"math_constrain"})
public final class MathConstrainFunct extends Function {
    public final List<Expr> param;

    public MathConstrainFunct(List<Expr> param, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(param != null);
        this.param = param;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.NUMBER;
    }

    @Override
    public String toString() {
        return "MathConstrainFunct [" + this.param + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));
        exprParams.add(new ExprParam(BlocklyConstants.LOW, BlocklyType.NUMBER_INT));
        exprParams.add(new ExprParam(BlocklyConstants.HIGH, BlocklyType.NUMBER_INT));
        List<Expr> params = helper.extractExprParameters(block, exprParams);
        return new MathConstrainFunct(params, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.param.get(0));
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.LOW, this.param.get(1));
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.HIGH, this.param.get(2));
        return jaxbDestination;
    }

}
