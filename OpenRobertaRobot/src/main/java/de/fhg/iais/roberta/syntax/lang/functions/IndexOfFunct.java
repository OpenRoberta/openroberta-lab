package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
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
 * This class represents <b>text_indexOf</b> and <b>lists_indexOf</b> blocks from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(Type, FunctionNames, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class IndexOfFunct<V> extends Function<V> {
    private final IIndexLocation location;
    private final List<Expr<V>> param;

    private IndexOfFunct(IIndexLocation name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TEXT_INDEX_OF_FUNCT"), properties, comment);
        Assert.isTrue(name != null && param != null);
        this.location = name;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link IndexOfFunct}. This instance is read only and can not be modified.
     *
     * @param name of the function; must be <b>not</b> null,
     * @param param list of parameters for the function; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link IndexOfFunct}
     */
    public static <V> IndexOfFunct<V> make(IIndexLocation name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new IndexOfFunct<V>(name, param, properties, comment);
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
        return BlocklyType.CAPTURED_TYPE;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitIndexOfFunct(this);
    }

    @Override
    public String toString() {
        return "IndexOfFunct [" + this.location + ", " + this.param + "]";
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
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.VALUE, BlocklyType.STRING));
        exprParams.add(new ExprParam(BlocklyConstants.FIND, BlocklyType.STRING));
        String op = helper.getOperation(block, BlocklyConstants.END);
        List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
        return IndexOfFunct.make(factory.getIndexLocation(op), params, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.END, getLocation().toString());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, getParam().get(0));
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.FIND, getParam().get(1));
        return jaxbDestination;
    }

}
