package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.IndexLocation;
import de.fhg.iais.roberta.ast.syntax.ListElementOperations;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>lists_getIndex</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(ListElementOperations, IndexLocation, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link IndexLocation} contains all allowed functions.
 */
public class ListGetIndex<V> extends Function<V> {
    private final ListElementOperations mode;
    private final IndexLocation location;

    private final List<Expr<V>> param;

    private ListGetIndex(ListElementOperations mode, IndexLocation name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.LIST_INDEX_OF, properties, comment);
        Assert.isTrue(mode != null && name != null && param != null);
        this.mode = mode;
        this.location = name;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ListGetIndex}. This instance is read only and can not be modified.
     *
     * @param mode; must be <b>not</b> null,
     * @param name of the function; must be <b>not</b> null,
     * @param param list of parameters for the function; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link ListGetIndex}
     */
    public static <V> ListGetIndex<V> make(
        ListElementOperations mode,
        IndexLocation name,
        List<Expr<V>> param,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new ListGetIndex<V>(mode, name, param, properties, comment);
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
        return visitor.visitListGetIndex(this);
    }

    @Override
    public String toString() {
        return "ListGetIndex [" + this.mode + ", " + this.location + ", " + this.param + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setAt(false);
        mutation.setStatement(getElementOperation().isStatment());
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getElementOperation().name());
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.WHERE, getLocation().name());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, getParam().get(0));
        if ( getParam().size() > 1 ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.AT, getParam().get(1));
            mutation.setAt(true);
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }
}
