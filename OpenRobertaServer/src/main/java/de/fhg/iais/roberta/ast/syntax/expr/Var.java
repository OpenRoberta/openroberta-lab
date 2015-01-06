package de.fhg.iais.roberta.ast.syntax.expr;

import javassist.bytecode.stackmap.TypeData.TypeVar;
import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>variables_set</b> and <b>variables_get</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for creating a variable.<br/>
 * <br>
 * User must provide name of the variable and type of the variable, if the variable is created before in the code TypeVar should be <b>NONE</b>.
 * To create an instance from this class use the method {@link #make(String, TypeVar, boolean, String)}.<br>
 */
public class Var<V> extends Expr<V> {
    private final String name;

    private Var(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.VAR, properties, comment);
        Assert.isTrue(!value.equals(""));
        this.name = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link Var}. This instance is read only and can not be modified.
     *
     * @param value name of the variable; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Var}
     */
    public static <V> Var<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Var<V>(value, properties, comment);
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
    public String toString() {
        return "Var [" + this.name + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitVar(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        AstJaxbTransformerHelper.addField(jaxbDestination, "VAR", getValue());
        return jaxbDestination;
    }
}
