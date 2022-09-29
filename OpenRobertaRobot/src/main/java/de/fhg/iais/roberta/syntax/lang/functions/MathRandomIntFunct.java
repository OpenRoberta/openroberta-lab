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

@NepoBasic(name = "MATH_RANDOM_INT_FUNCT", category = "FUNCTION", blocklyNames = {"math_random_int"})
public final class MathRandomIntFunct extends Function {
    public final List<Expr> param;

    public MathRandomIntFunct(List<Expr> param, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(param != null);
        this.param = param;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.LEFT;
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.NUMBER_INT;
    }

    @Override
    public String toString() {
        return "MathRandomIntFunct [" + this.param + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.FROM, BlocklyType.NUMBER_INT));
        exprParams.add(new ExprParam(BlocklyConstants.TO, BlocklyType.NUMBER_INT));
        List<Expr> params = helper.extractExprParameters(block, exprParams);
        return new MathRandomIntFunct(params, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FROM, this.param.get(0));
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TO, this.param.get(1));
        return jaxbDestination;
    }
}
