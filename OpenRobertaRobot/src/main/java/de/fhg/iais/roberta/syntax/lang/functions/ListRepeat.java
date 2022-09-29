package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
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

@NepoBasic(name = "LIST_REPEAT_FUNCT", category = "FUNCTION", blocklyNames = {"lists_repeat", "robLists_repeat"})
public final class ListRepeat extends Function {
    public final BlocklyType typeVar;
    public final List<Expr> param;

    public ListRepeat(BlocklyType typeVar, List<Expr> param, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(param != null);
        this.param = param;
        this.typeVar = typeVar;
        setReadOnly();
    }

    /**
     * @return element (what) to repeat from parameters
     */
    public Expr getElement() {
        return this.param.get(0);
    }

    /**
     * @return number (how often) to repeat from parameters
     */
    public Expr getCounter() {
        return this.param.get(1);
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
        return BlocklyType.ARRAY;
    }

    @Override
    public String toString() {
        return "ListRepeat [" + this.typeVar + ", " + this.param + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String filename = Jaxb2Ast.extractField(fields, BlocklyConstants.LIST_TYPE);
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.ITEM, BlocklyType.ARRAY));
        exprParams.add(new ExprParam(BlocklyConstants.NUM, BlocklyType.NUMBER_INT));
        List<Expr> params = helper.extractExprParameters(block, exprParams);
        return new ListRepeat(BlocklyType.get(filename), params, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();

        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setListType(this.typeVar.getBlocklyName());
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LIST_TYPE, this.typeVar.getBlocklyName());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ITEM, this.param.get(0));
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.NUM, this.param.get(1));
        return jaxbDestination;
    }
}
