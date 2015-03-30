package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.IndexLocation;
import de.fhg.iais.roberta.ast.syntax.ListElementOperations;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>lists_setIndex</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(IndexLocation, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link IndexLocation} contains all allowed functions.
 */
public class ListSetIndex<V> extends Function<V> {
    private final ListElementOperations mode;
    private final IndexLocation location;

    private final List<Expr<V>> param;

    private ListSetIndex(ListElementOperations mode, IndexLocation name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.LIST_SET_INDEX, properties, comment);
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
        ListElementOperations mode,
        IndexLocation name,
        List<Expr<V>> param,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new ListSetIndex<V>(mode, name, param, properties, comment);
    }

    /**
     * @return name of the function
     */
    public IndexLocation getLocation() {
        return this.location;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    public ListElementOperations getElementOperation() {
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitListSetIndex(this);
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
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String op = helper.extractField(fields, BlocklyConstants.MODE_);

        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.LIST_, String.class));
        exprParams.add(new ExprParam(BlocklyConstants.TO_, Integer.class));
        if ( block.getMutation().isAt() ) {
            exprParams.add(new ExprParam(BlocklyConstants.AT, Integer.class));
        }
        List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
        return ListSetIndex.make(
            ListElementOperations.get(op),
            IndexLocation.get(helper.extractField(fields, BlocklyConstants.WHERE)),
            params,
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setAt(false);
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getElementOperation().name());
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.WHERE, getLocation().name());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.LIST_, getParam().get(0));
        if ( getParam().size() > 2 ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.AT, getParam().get(2));
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.TO_, getParam().get(1));
            mutation.setAt(true);
        } else {
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.TO_, getParam().get(1));
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }

}
