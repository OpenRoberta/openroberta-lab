package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents <b>power</b> function from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(FunctionNames, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class MathPowerFunct<V> extends Expr<V> {
    private final FunctionNames functName;
    private final List<Expr<V>> param;

    private MathPowerFunct(FunctionNames name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("FUNCTIONS"), properties, comment);
        Assert.isTrue(name != null && param != null);
        this.functName = name;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link Binary}. This instance is read only and can not be modified.
     *
     * @param name of the function; must be <b>not</b> null,
     * @param param list of parameters for the function; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MathPowerFunct}
     */
    public static <V> MathPowerFunct<V> make(FunctionNames name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MathPowerFunct<V>(name, param, properties, comment);
    }

    /**
     * @return name of the function
     */
    public FunctionNames getFunctName() {
        return this.functName;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    @Override
    public int getPrecedence() {
        return this.functName.getPrecedence();
    }

    @Override
    public String toString() {
        return "MathPowerFunct [" + this.functName + ", " + this.param + "]";
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.NOTHING;
    }

    @Override
    public Assoc getAssoc() {
        return this.functName.getAssoc();
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitMathPowerFunct(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.OP, getFunctName().name());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.A, getParam().get(0));
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.B, getParam().get(1));
        return jaxbDestination;
    }
}
