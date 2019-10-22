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
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitListSetIndex(this);
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
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 2);
        String op = helper.extractField(fields, BlocklyConstants.MODE);

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
                factory.getIndexLocation(helper.extractField(fields, BlocklyConstants.WHERE)),
                params,
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setAt(false);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, getElementOperation().toString());
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.WHERE, getLocation().toString());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.LIST, getParam().get(0));
        if ( getParam().size() > 2 ) {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.AT, getParam().get(2));
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.TO, getParam().get(1));
            mutation.setAt(true);
        } else {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.TO, getParam().get(1));
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }

}
