package de.fhg.iais.roberta.syntax.lang.expr;

import java.math.BigInteger;
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
 * This class represents <b>robLists_create_with</b> and <b>lists_create_with</b> blocks from Blockly into the AST (abstract syntax tree). Object from this
 * class will generate code numerical value.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ListCreate<V> extends Expr<V> {
    private final BlocklyType typeVar;
    private final ExprList<V> exprList;

    private ListCreate(BlocklyType typeVar, ExprList<V> exprList, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LIST_CREATE"), properties, comment);
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
     * factory method: creates instance of {@link ListCreate}.<br>
     * <b>Main use: either testing or textual representation of programs (because in these cases no graphical regeneration is required.</b>
     *
     * @param name of the blockly type of the value that the expression should have.
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ListCreate}
     */
    public static <V> ListCreate<V> make(String blocklyName, BlocklyBlockProperties properties, BlocklyComment comment) {
        if ( blocklyName.trim().toUpperCase().equals("ANY") ) {
            return new ListCreate<V>(BlocklyType.ANY, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("COMPARABLE") ) {
            return new ListCreate<V>(BlocklyType.COMPARABLE, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ADDABLE") ) {
            return new ListCreate<V>(BlocklyType.ADDABLE, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY") ) {
            return new ListCreate<V>(BlocklyType.ARRAY, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_NUMBER") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_NUMBER, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_STRING") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_STRING, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_COLOUR") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_COLOUR, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_BOOLEAN") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_BOOLEAN, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_IMAGE") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_IMAGE, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_CONNECTION") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_CONNECTION, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("BOOLEAN") ) {
            return new ListCreate<V>(BlocklyType.BOOLEAN, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER") ) {
            return new ListCreate<V>(BlocklyType.NUMBER, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER_INT") ) {
            return new ListCreate<V>(BlocklyType.NUMBER_INT, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("STRING") ) {
            return new ListCreate<V>(BlocklyType.STRING, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("COLOR") ) {
            return new ListCreate<V>(BlocklyType.COLOR, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("IMAGE") ) {
            return new ListCreate<V>(BlocklyType.IMAGE, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("PREDEFINED_IMAGE") ) {
            return new ListCreate<V>(BlocklyType.PREDEFINED_IMAGE, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NULL") ) {
            return new ListCreate<V>(BlocklyType.NULL, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("REF") ) {
            return new ListCreate<V>(BlocklyType.REF, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("PRIM") ) {
            return new ListCreate<V>(BlocklyType.PRIM, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NOTHING") ) {
            return new ListCreate<V>(BlocklyType.NOTHING, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("VOID") ) {
            return new ListCreate<V>(BlocklyType.VOID, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("CONNECTION") ) {
            return new ListCreate<V>(BlocklyType.CONNECTION, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("CAPTURED_TYPE") ) {
            return new ListCreate<V>(BlocklyType.CAPTURED_TYPE, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("R") ) {
            return new ListCreate<V>(BlocklyType.R, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("S") ) {
            return new ListCreate<V>(BlocklyType.S, ExprList.make(), properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("T") ) {
            return new ListCreate<V>(BlocklyType.T, ExprList.make(), properties, comment);
        }
        return null;
    }

    /**
     * factory method: creates instance of {@link ListCreate}.<br>
     * <b>Main use: either testing or textual representation of programs (because in these cases no graphical regeneration is required.</b>
     *
     * @param name of the blockly type of the value that the expression should have.
     * @return read only object of class {@link ListCreate}
     */
    public static <V> ListCreate<V> make(String blocklyName) {
        if ( blocklyName.trim().toUpperCase().equals("ANY") ) {
            return new ListCreate<V>(BlocklyType.ANY, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("COMPARABLE") ) {
            return new ListCreate<V>(BlocklyType.COMPARABLE, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ADDABLE") ) {
            return new ListCreate<V>(BlocklyType.ADDABLE, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY") ) {
            return new ListCreate<V>(BlocklyType.ARRAY, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_NUMBER") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_NUMBER, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_STRING") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_STRING, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_COLOUR") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_COLOUR, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_BOOLEAN") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_BOOLEAN, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_IMAGE") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_IMAGE, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_CONNECTION") ) {
            return new ListCreate<V>(BlocklyType.ARRAY_CONNECTION, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("BOOLEAN") ) {
            return new ListCreate<V>(BlocklyType.BOOLEAN, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER") ) {
            return new ListCreate<V>(BlocklyType.NUMBER, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER_INT") ) {
            return new ListCreate<V>(BlocklyType.NUMBER_INT, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("STRING") ) {
            return new ListCreate<V>(BlocklyType.STRING, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("COLOR") ) {
            return new ListCreate<V>(BlocklyType.COLOR, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("IMAGE") ) {
            return new ListCreate<V>(BlocklyType.IMAGE, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("PREDEFINED_IMAGE") ) {
            return new ListCreate<V>(BlocklyType.PREDEFINED_IMAGE, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NULL") ) {
            return new ListCreate<V>(BlocklyType.NULL, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("REF") ) {
            return new ListCreate<V>(BlocklyType.REF, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("PRIM") ) {
            return new ListCreate<V>(BlocklyType.PRIM, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NOTHING") ) {
            return new ListCreate<V>(BlocklyType.NOTHING, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("VOID") ) {
            return new ListCreate<V>(BlocklyType.VOID, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("CONNECTION") ) {
            return new ListCreate<V>(BlocklyType.CONNECTION, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("CAPTURED_TYPE") ) {
            return new ListCreate<V>(BlocklyType.CAPTURED_TYPE, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("R") ) {
            return new ListCreate<V>(BlocklyType.R, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("S") ) {
            return new ListCreate<V>(BlocklyType.S, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("T") ) {
            return new ListCreate<V>(BlocklyType.T, ExprList.make(), BlocklyBlockProperties.make("1", "1"), null);
        }
        return null;
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
    protected V accept(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitListCreate(this);
    }

    @Override
    public BlocklyType getVarType() {
        return this.typeVar;
    }

    @Override
    public String toString() {
        return "ListCreate [" + this.typeVar + ", " + this.exprList + "]";
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
        return ListCreate
            .make(
                BlocklyType.get(filename),
                helper.blockToExprList(block, BlocklyType.ARRAY),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();

        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        ExprList<?> exprList = getValue();
        int numOfItems = exprList.get().size();
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numOfItems));
        mutation.setListType(getTypeVar().getBlocklyName());
        jaxbDestination.setMutation(mutation);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.LIST_TYPE, getTypeVar().getBlocklyName());
        for ( int i = 0; i < numOfItems; i++ ) {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.ADD + i, exprList.get().get(i));
        }
        return jaxbDestination;
    }
}
