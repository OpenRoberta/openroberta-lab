package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.typecheck.BlocklyType;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>lists_repeat</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class ListRepeat<V> extends Function<V> {
    private final BlocklyType typeVar;
    private final List<Expr<V>> param;

    private ListRepeat(BlocklyType typeVar, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.LIST_REPEAT_FUNCT, properties, comment);
        Assert.isTrue(param != null);
        this.param = param;
        this.typeVar = typeVar;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ListRepeat}. This instance is read only and can not be modified.
     *
     * @param param list of parameters for the function; must be <b>not</b> null,,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link ListRepeat}
     */
    public static <V> ListRepeat<V> make(BlocklyType typeVar, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ListRepeat<V>(typeVar, param, properties, comment);
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    /**
     * @return the typeVar
     */
    public BlocklyType getTypeVar() {
        return this.typeVar;
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitListRepeat(this);
    }

    @Override
    public String toString() {
        return "ListRepeat [" + this.typeVar + ", " + this.param + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String filename = helper.extractField(fields, BlocklyConstants.LIST_TYPE);
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.ITEM, List.class));
        exprParams.add(new ExprParam(BlocklyConstants.NUM, Integer.class));
        List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
        return ListRepeat.make(BlocklyType.get(filename), params, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();

        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setListType(this.typeVar.getBlocklyName());
        jaxbDestination.setMutation(mutation);
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.LIST_TYPE, this.typeVar.getBlocklyName());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.ITEM, getParam().get(0));
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.NUM, getParam().get(1));
        return jaxbDestination;
    }
}
