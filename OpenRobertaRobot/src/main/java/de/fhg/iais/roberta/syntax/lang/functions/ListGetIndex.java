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

@NepoBasic(name = "LIST_INDEX_OF", category = "FUNCTION", blocklyNames = {"robLists_getIndex", "lists_getIndex"})
public final class ListGetIndex extends Function {
    public final IListElementOperations mode;
    public final IIndexLocation location;
    public final String dataType;

    public final List<Expr> param;

    public ListGetIndex(
        IListElementOperations mode,
        IIndexLocation name,
        List<Expr> param,
        String dataType,
        BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(mode != null && name != null && param != null);
        this.mode = mode;
        this.location = name;
        this.param = param;
        this.dataType = dataType;
        setReadOnly();
    }

    public IListElementOperations getElementOperation() {
        return this.mode;
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
        return this.param.get(0).getVarType();
    }

    @Override
    public String toString() {
        return "ListGetIndex [" + this.mode + ", " + this.location + ", " + this.param + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<ExprParam> exprParams = new ArrayList<>();
        String op = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);
        exprParams.add(new ExprParam(BlocklyConstants.VALUE, BlocklyType.STRING));
        if ( block.getMutation().isAt() ) {
            exprParams.add(new ExprParam(BlocklyConstants.AT, BlocklyType.NUMBER_INT));
        }
        String dataType = block.getMutation().getDatatype();
        List<Expr> params = helper.extractExprParameters(block, exprParams);
        return new ListGetIndex(factory.getListElementOpertaion(op), factory.getIndexLocation(Jaxb2Ast.extractField(fields, BlocklyConstants.WHERE)), params, dataType, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setAt(false);
        mutation.setStatement(getElementOperation().isStatment());
        mutation.setDatatype(this.dataType);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, getElementOperation().toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.WHERE, this.location.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.param.get(0));
        if ( this.param.size() > 1 ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.AT, this.param.get(1));
            mutation.setAt(true);
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }
}
