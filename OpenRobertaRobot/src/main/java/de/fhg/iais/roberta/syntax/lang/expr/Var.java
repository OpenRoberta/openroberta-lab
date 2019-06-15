package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
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
 * This class represents the <b>variables_set</b> and <b>variables_get</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will
 * generate code for creating a variable.<br/>
 * <br>
 * User must provide name of the variable and type of the variable, if the variable is created before in the code TypeVar should be <b>NONE</b>. To create an
 * instance from this class use the method {@link #make(BlocklyType, String, boolean, String)}.<br>
 */
public class Var<V> extends Expr<V> {
    private final BlocklyType typeVar;
    private final String name;

    private Var(BlocklyType typeVar, String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("VAR"), properties, comment);
        Assert.isTrue(!value.equals("") && typeVar != null);
        this.name = value;
        this.typeVar = typeVar;
        setReadOnly();
    }

    /**
     * creates instance of {@link Var}. This instance is read only and can not be modified.
     *
     * @param typeVar type of the variable; must be <b>not</b> null,
     * @param value name of the variable; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Var}
     */
    public static <V> Var<V> make(BlocklyType typeVar, String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Var<V>(typeVar, value, properties, comment);
    }

    /**
     * factory method: create an AST instance of {@link Var}.<br>
     * <b>Main use: either testing or textual representation of programs (because in these cases no graphical regeneration is required.</b>
     *
     * @param name of the type of the variable; must be <b>not</b> null,
     * @param value name of the variable; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Var}
     */
    public static <V> Var<V> make(String blocklyName, String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        if ( blocklyName.trim().toUpperCase().equals("ANY") ) {
            return new Var<V>(BlocklyType.ANY, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("COMPARABLE") ) {
            return new Var<V>(BlocklyType.COMPARABLE, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ADDABLE") ) {
            return new Var<V>(BlocklyType.ADDABLE, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY") ) {
            return new Var<V>(BlocklyType.ARRAY, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_NUMBER") ) {
            return new Var<V>(BlocklyType.ARRAY_NUMBER, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_STRING") ) {
            return new Var<V>(BlocklyType.ARRAY_STRING, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_COLOUR") ) {
            return new Var<V>(BlocklyType.ARRAY_COLOUR, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_BOOLEAN") ) {
            return new Var<V>(BlocklyType.ARRAY_BOOLEAN, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_IMAGE") ) {
            return new Var<V>(BlocklyType.ARRAY_IMAGE, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_CONNECTION") ) {
            return new Var<V>(BlocklyType.ARRAY_CONNECTION, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("BOOLEAN") ) {
            return new Var<V>(BlocklyType.BOOLEAN, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER") ) {
            return new Var<V>(BlocklyType.NUMBER, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER_INT") ) {
            return new Var<V>(BlocklyType.NUMBER_INT, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("STRING") ) {
            return new Var<V>(BlocklyType.STRING, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("COLOR") ) {
            return new Var<V>(BlocklyType.COLOR, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("IMAGE") ) {
            return new Var<V>(BlocklyType.IMAGE, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("PREDEFINED_IMAGE") ) {
            return new Var<V>(BlocklyType.PREDEFINED_IMAGE, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NULL") ) {
            return new Var<V>(BlocklyType.NULL, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("REF") ) {
            return new Var<V>(BlocklyType.REF, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("PRIM") ) {
            return new Var<V>(BlocklyType.PRIM, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("NOTHING") ) {
            return new Var<V>(BlocklyType.NOTHING, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("VOID") ) {
            return new Var<V>(BlocklyType.VOID, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("CONNECTION") ) {
            return new Var<V>(BlocklyType.CONNECTION, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("CAPTURED_TYPE") ) {
            return new Var<V>(BlocklyType.CAPTURED_TYPE, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("R") ) {
            return new Var<V>(BlocklyType.R, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("S") ) {
            return new Var<V>(BlocklyType.S, value, properties, comment);
        }
        if ( blocklyName.trim().toUpperCase().equals("T") ) {
            return new Var<V>(BlocklyType.T, value, properties, comment);
        }
        return null;
    }

    /**
     * factory method: create an AST instance of {@link Var}.<br>
     * <b>Main use: either testing or textual representation of programs (because in these cases no graphical regeneration is required.</b>
     *
     * @param name of the type of the variable; must be <b>not</b> null,
     * @param value name of the variable; must be <b>non-empty</b> string,
     * @return read only object of class {@link Var}
     */
    public static <V> Var<V> make(String blocklyName, String value) {
        if ( blocklyName.trim().toUpperCase().equals("ANY") ) {
            return new Var<V>(BlocklyType.ANY, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("COMPARABLE") ) {
            return new Var<V>(BlocklyType.COMPARABLE, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ADDABLE") ) {
            return new Var<V>(BlocklyType.ADDABLE, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY") ) {
            return new Var<V>(BlocklyType.ARRAY, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_NUMBER") ) {
            return new Var<V>(BlocklyType.ARRAY_NUMBER, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_STRING") ) {
            return new Var<V>(BlocklyType.ARRAY_STRING, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_COLOUR") ) {
            return new Var<V>(BlocklyType.ARRAY_COLOUR, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_BOOLEAN") ) {
            return new Var<V>(BlocklyType.ARRAY_BOOLEAN, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_IMAGE") ) {
            return new Var<V>(BlocklyType.ARRAY_IMAGE, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("ARRAY_CONNECTION") ) {
            return new Var<V>(BlocklyType.ARRAY_CONNECTION, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("BOOLEAN") ) {
            return new Var<V>(BlocklyType.BOOLEAN, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER") ) {
            return new Var<V>(BlocklyType.NUMBER, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NUMBER_INT") ) {
            return new Var<V>(BlocklyType.NUMBER_INT, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("STRING") ) {
            return new Var<V>(BlocklyType.STRING, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("COLOR") ) {
            return new Var<V>(BlocklyType.COLOR, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("IMAGE") ) {
            return new Var<V>(BlocklyType.IMAGE, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("PREDEFINED_IMAGE") ) {
            return new Var<V>(BlocklyType.PREDEFINED_IMAGE, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NULL") ) {
            return new Var<V>(BlocklyType.NULL, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("REF") ) {
            return new Var<V>(BlocklyType.REF, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("PRIM") ) {
            return new Var<V>(BlocklyType.PRIM, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("NOTHING") ) {
            return new Var<V>(BlocklyType.NOTHING, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("VOID") ) {
            return new Var<V>(BlocklyType.VOID, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("CONNECTION") ) {
            return new Var<V>(BlocklyType.CONNECTION, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("CAPTURED_TYPE") ) {
            return new Var<V>(BlocklyType.CAPTURED_TYPE, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("R") ) {
            return new Var<V>(BlocklyType.R, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("S") ) {
            return new Var<V>(BlocklyType.S, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        if ( blocklyName.trim().toUpperCase().equals("T") ) {
            return new Var<V>(BlocklyType.T, value, BlocklyBlockProperties.make("1", "1"), null);
        }
        return null;
    }

    /**
     * @return name of the variable
     */
    public String getValue() {
        return this.name;
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
        return this.typeVar;
    }

    @Override
    public String toString() {
        return "Var [" + this.name + "]";
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitVar(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return helper.extractVar(block);
    }

    @Override
    public Block astToBlock() {

        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setDatatype(getVarType().getBlocklyName());
        jaxbDestination.setMutation(mutation);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.VAR, getValue());
        return jaxbDestination;
    }
}
