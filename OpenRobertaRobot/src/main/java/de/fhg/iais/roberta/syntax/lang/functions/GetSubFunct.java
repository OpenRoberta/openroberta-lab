package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.general.IMode;
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

/**
 * This class represents the <b>text_getSubstring</b> block
 */
@NepoBasic(name = "GET_SUB_FUNCT", category = "FUNCTION", blocklyNames = {"lists_getSublist", "robLists_getSublist"})
public final class GetSubFunct extends Function {
    public final FunctionNames functName;
    public final List<Expr> param;
    public final List<IMode> strParam;

    public GetSubFunct(FunctionNames name, List<IMode> strParam, List<Expr> param, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(name != null && param != null && strParam != null);
        this.functName = name;
        this.param = param;
        this.strParam = strParam;
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
        return BlocklyType.ARRAY;
    }

    @Override
    public String toString() {
        return "GetSubFunct [" + this.functName + ", " + this.strParam + ", " + this.param + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<IMode> strParams = new ArrayList<IMode>();
        strParams.add(factory.getIndexLocation(Jaxb2Ast.extractField(fields, BlocklyConstants.WHERE1)));
        strParams.add(factory.getIndexLocation(Jaxb2Ast.extractField(fields, BlocklyConstants.WHERE2)));
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.LIST, BlocklyType.STRING));
        if ( block.getMutation().isAt1() ) {
            exprParams.add(new ExprParam(BlocklyConstants.AT1, BlocklyType.NUMBER_INT));
        }
        if ( block.getMutation().isAt2() ) {
            exprParams.add(new ExprParam(BlocklyConstants.AT2, BlocklyType.NUMBER_INT));
        }
        List<Expr> params = helper.extractExprParameters(block, exprParams);
        return new GetSubFunct(FunctionNames.GET_SUBLIST, strParams, params, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();

        mutation.setAt1(false);
        mutation.setAt2(false);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.WHERE1, this.strParam.get(0).toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.WHERE2, this.strParam.get(1).toString());
        if ( this.functName == FunctionNames.GET_SUBLIST ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.LIST, this.param.get(0));
        } else {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.STRING, this.param.get(0));
        }
        if ( this.strParam.get(0).toString().equals("FROM_START") || this.strParam.get(0).toString().equals("FROM_END") ) {
            mutation.setAt1(true);
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.AT1, this.param.get(1));
        }
        if ( this.strParam.get(1).toString().equals("FROM_START") || this.strParam.get(1).toString().equals("FROM_END") ) {
            mutation.setAt2(true);
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.AT2, this.param.get(this.param.size() - 1));
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }
}
