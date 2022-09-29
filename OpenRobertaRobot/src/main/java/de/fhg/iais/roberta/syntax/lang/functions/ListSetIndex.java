package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
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

@NepoBasic(name = "LIST_SET_INDEX", category = "FUNCTION", blocklyNames = {"lists_setIndex", "robLists_setIndex"})
public final class ListSetIndex extends Function {
    public final IListElementOperations mode;
    public final IIndexLocation location;

    public final List<Expr> param;

    public ListSetIndex(IListElementOperations mode, IIndexLocation name, List<Expr> param, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(mode != null && name != null && param != null);
        this.mode = mode;
        this.location = name;
        this.param = param;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.LEFT;
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.VOID;
    }

    @Override
    public String toString() {
        return "ListSetIndex [" + this.mode + ", " + this.location + ", " + this.param + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String op = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);

        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.LIST, BlocklyType.STRING));
        exprParams.add(new ExprParam(BlocklyConstants.TO, BlocklyType.NUMBER_INT));
        if ( block.getMutation().isAt() ) {
            exprParams.add(new ExprParam(BlocklyConstants.AT, BlocklyType.NUMBER_INT));
        }
        List<Expr> params = helper.extractExprParameters(block, exprParams);
        return new ListSetIndex(factory.getListElementOpertaion(op), factory.getIndexLocation(Jaxb2Ast.extractField(fields, BlocklyConstants.WHERE)), params, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setAt(false);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.mode.toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.WHERE, this.location.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.LIST, this.param.get(0));
        if ( this.param.size() > 2 ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.AT, this.param.get(2));
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TO, this.param.get(1));
            mutation.setAt(true);
        } else {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TO, this.param.get(1));
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }

}
