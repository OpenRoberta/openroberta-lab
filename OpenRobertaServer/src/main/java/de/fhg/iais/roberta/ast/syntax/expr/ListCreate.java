package de.fhg.iais.roberta.ast.syntax.expr;

import java.math.BigInteger;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.typecheck.BlocklyType;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents <b>robLists_create_with</b> and <b>lists_create_with</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code numerical value.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ListCreate<V> extends Expr<V> {
    private final BlocklyType typeVar;
    private final ExprList<V> exprList;

    private ListCreate(BlocklyType typeVar, ExprList<V> exprList, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.LIST_CREATE, properties, comment);
        Assert.isTrue(exprList != null && exprList.isReadOnly() && typeVar != null);
        this.exprList = exprList;
        this.typeVar = typeVar;
        setReadOnly();
    }

    /**
     * creates instance of {@link ListCreate}. This instance is read only and can not be modified.
     *
     * @param exprList; must be <b>not</b> null and <b>read only</b>,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ListCreate}
     */
    public static <V> ListCreate<V> make(BlocklyType typeVar, ExprList<V> exprList, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ListCreate<V>(typeVar, exprList, properties, comment);
    }

    /**
     * @return value of the numerical constant
     */
    public ExprList<V> getValue() {
        return this.exprList;
    }

    /**
     * @return the typeVar
     */
    public BlocklyType getTypeVar() {
        return this.typeVar;
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitListCreate(this);
    }

    @Override
    public String toString() {
        return "ListCreate [" + this.typeVar + ", " + this.exprList + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();

        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        ExprList<?> exprList = getValue();
        int numOfItems = (exprList.get().size());
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numOfItems));
        jaxbDestination.setMutation(mutation);
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.LIST_TYPE, getTypeVar().getBlocklyName());
        for ( int i = 0; i < numOfItems; i++ ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.ADD + i, exprList.get().get(i));
        }
        return jaxbDestination;
    }
}
