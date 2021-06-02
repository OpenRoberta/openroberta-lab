package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robGlobalvariables_declare</b> blocks from Blockly in the AST (abstract syntax tree). Object from this class will generate
 * code for creating a variable.
 */
public class VarDeclaration<V> extends Expr<V> {
    private final BlocklyType typeVar;
    private final String name;
    private final Phrase<V> value;
    private final boolean next;
    private final boolean global;
    private final static String CODE_SAFE_PREFIX = "___";

    private VarDeclaration(
        BlocklyType typeVar,
        String name,
        Phrase<V> value,
        boolean next,
        boolean global,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("VAR_DECLARATION"), properties, comment);
        Assert.isTrue(!name.equals("") && typeVar != null && value.isReadOnly());
        this.name = name;
        this.typeVar = typeVar;
        this.value = value;
        this.next = next;
        this.global = global;
        setReadOnly();
    }

    /**
     * creates instance of {@link VarDeclaration}. This instance is read only and can not be modified.
     *
     * @param typeVar type of the variable; must be <b>not</b> null,
     * @param name of the variable; must be <b>non-empty</b> string,
     * @param value initialization value of the variable; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link VarDeclaration}
     */
    public static <V> VarDeclaration<V> make(
        BlocklyType typeVar,
        String name,
        Expr<V> value,
        boolean next,
        boolean global,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new VarDeclaration<>(typeVar, name, value, next, global, properties, comment);
    }

    /**
     * @return type of the variable
     */
    public BlocklyType getTypeVar() {
        return this.typeVar;
    }

    /**
     * @return name of the variable
     */
    public String getName() {
        return this.name;
    }

    public String getCodeSafeName() {
        return CODE_SAFE_PREFIX + this.name;
    }

    /**
     * @return the value
     */
    public Phrase<V> getValue() {
        return this.value;
    }

    /**
     * @return the next
     */
    public boolean isNext() {
        return this.next;
    }

    /**
     * @return the global
     */
    public boolean isGlobal() {
        return this.global;
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
        return "VarDeclaration [" + this.typeVar + ", " + this.name + ", " + this.value + ", " + this.next + ", " + this.global + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        boolean isGlobalVariable = block.getType().equals(BlocklyConstants.ROB_LOCAL_VARIABLES_DECLARE) ? false : true;
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        BlocklyType typeVar = BlocklyType.get(Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE));
        String name = Jaxb2Ast.extractField(fields, BlocklyConstants.VAR);
        Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, typeVar));
        boolean next = block.getMutation().isNext();

        return VarDeclaration
            .make(
                typeVar,
                name,
                Jaxb2Ast.convertPhraseToExpr(expr),
                next,
                isGlobalVariable,
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {

        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setNext(this.next);
        mutation.setDeclarationType(getTypeVar().getBlocklyName());
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VAR, getName());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, getTypeVar().getBlocklyName());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);

        return jaxbDestination;
    }

}
