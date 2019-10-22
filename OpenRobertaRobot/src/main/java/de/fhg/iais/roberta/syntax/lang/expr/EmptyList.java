package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>lists_create_empty</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * creating <b>empty list</b>.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class EmptyList<V> extends Expr<V> {
    private final BlocklyType typeVar;

    private EmptyList(BlocklyType typeVar, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("EMPTY_LIST"), properties, comment);
        Assert.isTrue(typeVar != null);
        this.typeVar = typeVar;
        setReadOnly();
    }

    /**
     * creates instance of {@link EmptyList}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NullConst}
     */
    public static <V> EmptyList<V> make(BlocklyType typeVar, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new EmptyList<V>(typeVar, properties, comment);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String filename = helper.extractField(fields, BlocklyConstants.LIST_TYPE);
        return EmptyList.make(BlocklyType.get(filename), helper.extractBlockProperties(block), helper.extractComment(block));
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
    public BlocklyType getVarType() {
        return BlocklyType.ARRAY;
    }

    @Override
    public String toString() {
        return "EmptyList [" + this.typeVar + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitEmptyList(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setListType(getTypeVar().getBlocklyName());
        jaxbDestination.setMutation(mutation);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.LIST_TYPE, getTypeVar().getBlocklyName());
        return jaxbDestination;
    }

}
