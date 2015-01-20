package de.fhg.iais.roberta.ast.syntax.expr;

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
 * This class represents the <b>robGlobalvariables_declare</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for creating a variable.<br/>
 * <br>
 * User must provide name of the variable, type of the variable and initial value.
 * To create an instance from this class use the method {@link #make(BlocklyType, String, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class VarDeclaration<V> extends Expr<V> {
    private final BlocklyType typeVar;
    private final String name;
    private final Expr<V> value;
    private final boolean next;
    private final boolean global;

    private VarDeclaration(
        BlocklyType typeVar,
        String name,
        Expr<V> value,
        boolean next,
        boolean global,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(Phrase.Kind.VAR, properties, comment);
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
        return new VarDeclaration<V>(typeVar, name, value, next, global, properties, comment);
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

    /**
     * @return the value
     */
    public Expr<V> getValue() {
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitVarDeclaration(this);
    }

    @Override
    public String toString() {
        return "VarDeclaration [" + this.typeVar + ", " + this.name + ", " + this.value + ", " + this.next + ", " + this.global + "]";
    }

    @Override
    public Block astToBlock() {

        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setNext(this.next);
        mutation.setDeclarationType(getTypeVar().getBlocklyName());
        jaxbDestination.setMutation(mutation);

        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.VAR, getName());
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.TYPE, getTypeVar().getBlocklyName());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);

        return jaxbDestination;
    }
}
