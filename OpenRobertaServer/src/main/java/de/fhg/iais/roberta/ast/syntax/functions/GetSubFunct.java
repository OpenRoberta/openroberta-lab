package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>text_getSubstring</b> and blocks <b>lists_getSublist</b> from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(Functions, List, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link Functions} contains all allowed functions.
 */
public class GetSubFunct<V> extends Function<V> {
    private final Functions functName;
    private final List<Expr<V>> param;
    private final List<String> strParam;

    private GetSubFunct(Functions name, List<String> strParam, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.GET_SUB_FUNCT, properties, comment);
        Assert.isTrue(name != null && param != null && strParam != null);
        this.functName = name;
        this.param = param;
        this.strParam = strParam;
        setReadOnly();
    }

    /**
     * Creates instance of {@link GetSubFunct}. This instance is read only and can not be modified.
     *
     * @param name of the function,
     * @param param list of expression parameters for the function,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @param strParam list of string parameters for the function
     * @return read only object of class {@link GetSubFunct}
     */
    public static <V> GetSubFunct<V> make(Functions name, List<String> strParam, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GetSubFunct<V>(name, strParam, param, properties, comment);
    }

    /**
     * @return name of the function
     */
    public Functions getFunctName() {
        return this.functName;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    /**
     * @return list of string parameters
     */
    public List<String> getStrParam() {
        return this.strParam;
    }

    @Override
    public int getPrecedence() {
        return this.functName.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.functName.getAssoc();
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitGetSubFunct(this);
    }

    @Override
    public String toString() {
        return "GetSubFunct [" + this.functName + ", " + this.strParam + ", " + this.param + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();

        mutation.setAt1(false);
        mutation.setAt2(false);
        AstJaxbTransformerHelper.addField(jaxbDestination, "WHERE1", getStrParam().get(0));
        AstJaxbTransformerHelper.addField(jaxbDestination, "WHERE2", getStrParam().get(1));
        if ( getFunctName() == Functions.GET_SUBLIST ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, "LIST", getParam().get(0));
        } else {
            AstJaxbTransformerHelper.addValue(jaxbDestination, "STRING", getParam().get(0));
        }
        if ( Functions.get(getStrParam().get(0)) == Functions.FROM_START || Functions.get(getStrParam().get(0)) == Functions.FROM_END ) {
            mutation.setAt1(true);
            AstJaxbTransformerHelper.addValue(jaxbDestination, "AT1", getParam().get(1));
        }
        if ( Functions.get(getStrParam().get(1)) == Functions.FROM_START || Functions.get(getStrParam().get(1)) == Functions.FROM_END ) {
            mutation.setAt2(true);
            AstJaxbTransformerHelper.addValue(jaxbDestination, "AT2", getParam().get(getParam().size() - 1));
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;

    }
}
