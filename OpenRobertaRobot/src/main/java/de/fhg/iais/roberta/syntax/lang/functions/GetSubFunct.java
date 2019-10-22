package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.general.IMode;
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
 * This class represents the <b>text_getSubstring</b> and blocks <b>lists_getSublist</b> from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(FunctionNames, List, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class GetSubFunct<V> extends Function<V> {
    private final FunctionNames functName;
    private final List<Expr<V>> param;
    private final List<IMode> strParam;

    private GetSubFunct(FunctionNames name, List<IMode> strParam, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("GET_SUB_FUNCT"), properties, comment);
        Assert.isTrue(name != null && param != null && strParam != null);
        this.functName = name;
        this.param = param;
        this.strParam = strParam;
        setReadOnly();
    }

    /**
     * Creates instance of {@link GetSubFunct}. This instance is read only and can not be modified.
     *
     * @param name of the function; must be <b>not</b> null,
     * @param param list of expression parameters for the function; must be <b>not</b> null,,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @param strParam list of string parameters for the function; must be <b>not</b> null,
     * @return read only object of class {@link GetSubFunct}
     */
    public static <V> GetSubFunct<V> make(
        FunctionNames name,
        List<IMode> strParam,
        List<Expr<V>> param,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new GetSubFunct<V>(name, strParam, param, properties, comment);
    }

    /**
     * @return name of the function
     */
    public FunctionNames getFunctName() {
        return this.functName;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    /**
     * @return list of string parameters
     */
    public List<IMode> getStrParam() {
        return this.strParam;
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitGetSubFunct(this);
    }

    @Override
    public String toString() {
        return "GetSubFunct [" + this.functName + ", " + this.strParam + ", " + this.param + "]";
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
        List<IMode> strParams = new ArrayList<IMode>();
        strParams.add(factory.getIndexLocation(helper.extractField(fields, BlocklyConstants.WHERE1)));
        strParams.add(factory.getIndexLocation(helper.extractField(fields, BlocklyConstants.WHERE2)));
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.LIST, BlocklyType.STRING));
        if ( block.getMutation().isAt1() ) {
            exprParams.add(new ExprParam(BlocklyConstants.AT1, BlocklyType.NUMBER_INT));
        }
        if ( block.getMutation().isAt2() ) {
            exprParams.add(new ExprParam(BlocklyConstants.AT2, BlocklyType.NUMBER_INT));
        }
        List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
        return GetSubFunct.make(FunctionNames.GET_SUBLIST, strParams, params, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();

        mutation.setAt1(false);
        mutation.setAt2(false);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.WHERE1, getStrParam().get(0).toString());
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.WHERE2, getStrParam().get(1).toString());
        if ( getFunctName() == FunctionNames.GET_SUBLIST ) {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.LIST, getParam().get(0));
        } else {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.STRING, getParam().get(0));
        }
        if ( getStrParam().get(0).toString().equals("FROM_START") || getStrParam().get(0).toString().equals("FROM_END") ) {
            mutation.setAt1(true);
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.AT1, getParam().get(1));
        }
        if ( getStrParam().get(1).toString().equals("FROM_START") || getStrParam().get(1).toString().equals("FROM_END") ) {
            mutation.setAt2(true);
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.AT2, getParam().get(getParam().size() - 1));
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }
}
