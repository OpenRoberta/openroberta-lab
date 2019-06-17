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
     * @param typeVar type of the value that the empty list should have.
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NullConst}
     */
    public static <V> EmptyList<V> make(BlocklyType typeVar, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new EmptyList<V>(typeVar, properties, comment);
    }

    /**
     * factory method: creates instance of {@link EmptyList}.<br>
     * <b>Main use: either testing or textual representation of programs (because in these cases no graphical regeneration is required.</b>
     *
     * @param typeVar type of the value that the empty list should have.
     * @return read only object of class {@link NullConst}
     */
    public static <V> EmptyList<V> make(BlocklyType typeVar) {
        return new EmptyList<V>(typeVar, BlocklyBlockProperties.make("1", "1"), null);
    }

    /**
     * factory method: creates instance of {@link EmptyList}.<br>
     * <b>Main use: either testing or textual representation of programs (because in these cases no graphical regeneration is required.</b>
     *
     * @param name of the blockly type of the value that the missing expression should have.
     * @return read only object of class {@link NullConst}
     */
    public static <V> EmptyList<V> make(String blocklyName) {
        if ( blocklyName.trim().toUpperCase().equals("ANY") ) {
            return new EmptyList<V>(BlocklyType.ANY, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("COMPARABLE") ) {
            return new EmptyList<V>(BlocklyType.COMPARABLE, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ADDABLE") ) {
            return new EmptyList<V>(BlocklyType.ADDABLE, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY") ) {
            return new EmptyList<V>(BlocklyType.ARRAY, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_NUMBER") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_NUMBER, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_STRING") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_STRING, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_COLOUR") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_COLOUR, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_BOOLEAN") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_BOOLEAN, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_IMAGE") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_IMAGE, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_CONNECTION") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_CONNECTION, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("BOOLEAN") ) {
            return new EmptyList<V>(BlocklyType.BOOLEAN, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER") ) {
            return new EmptyList<V>(BlocklyType.NUMBER, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER_INT") ) {
            return new EmptyList<V>(BlocklyType.NUMBER_INT, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("STRING") ) {
            return new EmptyList<V>(BlocklyType.STRING, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("COLOR") ) {
            return new EmptyList<V>(BlocklyType.COLOR, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("IMAGE") ) {
            return new EmptyList<V>(BlocklyType.IMAGE, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("PREDEFINED_IMAGE") ) {
            return new EmptyList<V>(BlocklyType.PREDEFINED_IMAGE, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NULL") ) {
            return new EmptyList<V>(BlocklyType.NULL, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("REF") ) {
            return new EmptyList<V>(BlocklyType.REF, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("PRIM") ) {
            return new EmptyList<V>(BlocklyType.PRIM, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NOTHING") ) {
            return new EmptyList<V>(BlocklyType.NOTHING, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("VOID") ) {
            return new EmptyList<V>(BlocklyType.VOID, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("CONNECTION") ) {
            return new EmptyList<V>(BlocklyType.CONNECTION, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("CAPTURED_TYPE") ) {
            return new EmptyList<V>(BlocklyType.CAPTURED_TYPE, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("R") ) {
            return new EmptyList<V>(BlocklyType.R, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("S") ) {
            return new EmptyList<V>(BlocklyType.S, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("T") ) {
            return new EmptyList<V>(BlocklyType.T, BlocklyBlockProperties.make("1", "1"), null);
        }
        return null;
    }

    /**
     * factory method: creates instance of {@link EmptyList}.<br>
     * <b>Main use: either testing or textual representation of programs (because in these cases no graphical regeneration is required.</b>
     *
     * @param name of the blockly type of the value that the missing expression should have.
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NullConst}
     */
    public static <V> EmptyList<V> make(String blocklyName, BlocklyBlockProperties properties, BlocklyComment comment) {
        if ( blocklyName.trim().toUpperCase().equals("ANY") ) {
            return new EmptyList<V>(BlocklyType.ANY, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("COMPARABLE") ) {
            return new EmptyList<V>(BlocklyType.COMPARABLE, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ADDABLE") ) {
            return new EmptyList<V>(BlocklyType.ADDABLE, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY") ) {
            return new EmptyList<V>(BlocklyType.ARRAY, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_NUMBER") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_NUMBER, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_STRING") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_STRING, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_COLOUR") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_COLOUR, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_BOOLEAN") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_BOOLEAN, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_IMAGE") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_IMAGE, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_CONNECTION") ) {
            return new EmptyList<V>(BlocklyType.ARRAY_CONNECTION, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("BOOLEAN") ) {
            return new EmptyList<V>(BlocklyType.BOOLEAN, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER") ) {
            return new EmptyList<V>(BlocklyType.NUMBER, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER_INT") ) {
            return new EmptyList<V>(BlocklyType.NUMBER_INT, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("STRING") ) {
            return new EmptyList<V>(BlocklyType.STRING, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("COLOR") ) {
            return new EmptyList<V>(BlocklyType.COLOR, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("IMAGE") ) {
            return new EmptyList<V>(BlocklyType.IMAGE, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("PREDEFINED_IMAGE") ) {
            return new EmptyList<V>(BlocklyType.PREDEFINED_IMAGE, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NULL") ) {
            return new EmptyList<V>(BlocklyType.NULL, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("REF") ) {
            return new EmptyList<V>(BlocklyType.REF, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("PRIM") ) {
            return new EmptyList<V>(BlocklyType.PRIM, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NOTHING") ) {
            return new EmptyList<V>(BlocklyType.NOTHING, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("VOID") ) {
            return new EmptyList<V>(BlocklyType.VOID, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("CONNECTION") ) {
            return new EmptyList<V>(BlocklyType.CONNECTION, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("CAPTURED_TYPE") ) {
            return new EmptyList<V>(BlocklyType.CAPTURED_TYPE, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("R") ) {
            return new EmptyList<V>(BlocklyType.R, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("S") ) {
            return new EmptyList<V>(BlocklyType.S, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("T") ) {
            return new EmptyList<V>(BlocklyType.T, properties, comment);
        }
        return null;
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
    protected V accept(IVisitor<V> visitor) {
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
