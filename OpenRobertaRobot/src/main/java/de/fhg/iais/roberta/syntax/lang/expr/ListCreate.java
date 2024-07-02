package de.fhg.iais.roberta.syntax.lang.expr;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents <b>robLists_create_with</b> and <b>lists_create_with</b> blocks from Blockly into the AST (abstract syntax tree). Object from this
 * class will generate code numerical value.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyProperties, BlocklyComment)}.<br>
 */
@NepoBasic(name = "LIST_CREATE", category = "EXPR", blocklyNames = {"robLists_create_with", "lists_create_with"})
public final class ListCreate extends Expr {
    public final ExprList exprList;

    public ListCreate(BlocklyType typeVar, ExprList exprList, BlocklyProperties properties) {
        super(properties, typeVar);
        Assert.isTrue(exprList != null && exprList.isReadOnly() && typeVar != null);
        this.exprList = exprList;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public String toString() {
        return "ListCreate [" + this.getBlocklyType() + ", " + this.exprList + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String filename = Jaxb2Ast.extractField(fields, BlocklyConstants.LIST_TYPE);
        return new ListCreate(BlocklyType.get(filename), helper.blockToExprList(block, BlocklyType.ARRAY), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public List<Block> ast2xml() {
        String blocklyName = this.getBlocklyType().getBlocklyName();
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        ExprList exprList = this.exprList;
        int numOfItems = exprList.get().size();
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numOfItems));
        mutation.setListType(blocklyName);
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LIST_TYPE, blocklyName);
        for ( int i = 0; i < numOfItems; i++ ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ADD + i, exprList.get().get(i));
        }
        return Collections.singletonList(jaxbDestination);
    }
}
