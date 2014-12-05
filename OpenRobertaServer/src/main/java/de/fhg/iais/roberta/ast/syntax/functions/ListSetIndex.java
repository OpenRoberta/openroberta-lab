package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>lists_setIndex</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(FunctionNames, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class ListSetIndex<V> extends Function<V> {
    private final Mode mode;
    private final FunctionNames functName;

    private final List<Expr<V>> param;

    private ListSetIndex(Mode mode, FunctionNames name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.LIST_SET_INDEX, properties, comment);
        Assert.isTrue(mode != null && name != null && param != null);
        this.mode = mode;
        this.functName = name;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ListSetIndex}. This instance is read only and can not be modified.
     *
     * @param mode
     * @param name of the function,
     * @param param list of parameters for the function,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link ListSetIndex}
     */
    public static <V> ListSetIndex<V> make(Mode mode, FunctionNames name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ListSetIndex<V>(mode, name, param, properties, comment);
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

    public Mode getMode() {
        return this.mode;
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.LEFT;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitListSetIndex(this);
    }

    @Override
    public String toString() {
        return "ListSetIndex [" + this.mode + ", " + this.functName + ", " + this.param + "]";
    }

    public static enum Mode {
        SET(), INSERT();

        private final String[] values;

        private Mode(String... strings) {
            this.values = strings;
        }

        /**
         * get mode from {@link Mode} from string parameter. It is possible for one function to have multiple string mappings.
         * Throws exception if the operator does not exists.
         *
         * @param functName of the function
         * @return function from the enum {@link Mode}
         */
        public static Mode get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid mode name: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Mode funct : Mode.values() ) {
                if ( funct.toString().equals(sUpper) ) {
                    return funct;
                }
                for ( String value : funct.values ) {
                    if ( sUpper.equals(value) ) {
                        return funct;
                    }
                }
            }
            throw new DbcException("Invalid mode name: " + s);
        }
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setAt(false);
        AstJaxbTransformerHelper.addField(jaxbDestination, "MODE", getMode().name());
        AstJaxbTransformerHelper.addField(jaxbDestination, "WHERE", getFunctName().name());
        AstJaxbTransformerHelper.addValue(jaxbDestination, "LIST", getParam().get(0));
        if ( getParam().size() > 2 ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, "AT", getParam().get(1));
            AstJaxbTransformerHelper.addValue(jaxbDestination, "TO", getParam().get(2));
            mutation.setAt(true);
        } else {
            AstJaxbTransformerHelper.addValue(jaxbDestination, "TO", getParam().get(1));
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }

}
