package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

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
    private final static String CODE_SAFE_PREFIX = "___";

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
        return new Var<>(typeVar, value, properties, comment);
    }

    /**
     * @return name of the variable
     */
    public String getValue() {
        return this.name;
    }

    public String getCodeSafeName() {
        return CODE_SAFE_PREFIX + this.name;
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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        return Jaxb2Ast.extractVar(block);
    }

    @Override
    public Block astToBlock() {

        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setDatatype(getVarType().getBlocklyName());
        jaxbDestination.setMutation(mutation);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VAR, getValue());
        return jaxbDestination;
    }
}
