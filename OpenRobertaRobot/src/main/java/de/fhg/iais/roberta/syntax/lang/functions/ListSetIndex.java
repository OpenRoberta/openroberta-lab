package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>lists_setIndex</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(IndexLocation, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link IndexLocation} contains all allowed functions.
 */
public class ListSetIndex<V> extends Function<V> {
    private final IListElementOperations mode;
    private final IIndexLocation location;

    private final List<Expr<V>> param;

    private ListSetIndex(IListElementOperations mode, IIndexLocation name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LIST_SET_INDEX"), properties, comment);
        Assert.isTrue(mode != null && name != null && param != null);
        this.mode = mode;
        this.location = name;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ListSetIndex}. This instance is read only and can not be modified.
     *
     * @param mode; must be <b>not</b> null,
     * @param name of the function; must be <b>not</b> null,
     * @param param list of parameters for the function; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link ListSetIndex}
     */
    public static <V> ListSetIndex<V> make(
        IListElementOperations mode,
        IIndexLocation name,
        List<Expr<V>> param,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new ListSetIndex<V>(mode, name, param, properties, comment);
    }

    /**
     * @return name of the function
     */
    public IIndexLocation getLocation() {
        return this.location;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
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
        return BlocklyType.VOID;
    }

    @Override
    public String toString() {
        return "ListSetIndex [" + this.mode + ", " + this.location + ", " + this.param + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String op = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);

        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.LIST, BlocklyType.STRING));
        exprParams.add(new ExprParam(BlocklyConstants.TO, BlocklyType.NUMBER_INT));
        if ( block.getMutation().isAt() ) {
            exprParams.add(new ExprParam(BlocklyConstants.AT, BlocklyType.NUMBER_INT));
        }
        List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
        return ListSetIndex
            .make(
                factory.getListElementOpertaion(op),
                factory.getIndexLocation(Jaxb2Ast.extractField(fields, BlocklyConstants.WHERE)),
                params,
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setAt(false);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, getElementOperation().toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.WHERE, getLocation().toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.LIST, getParam().get(0));
        if ( getParam().size() > 2 ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.AT, getParam().get(2));
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TO, getParam().get(1));
            mutation.setAt(true);
        } else {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TO, getParam().get(1));
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }

}
