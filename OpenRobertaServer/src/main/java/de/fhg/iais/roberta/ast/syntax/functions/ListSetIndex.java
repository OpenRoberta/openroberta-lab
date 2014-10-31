package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>lists_setIndex</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(Functions, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link Functions} contains all allowed functions.
 */
public class ListSetIndex<V> extends Function<V> {
    private final Mode mode;
    private final Functions functName;

    private final List<Expr<V>> param;

    private ListSetIndex(Mode mode, Functions name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
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
    public static <V> ListSetIndex<V> make(Mode mode, Functions name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ListSetIndex<V>(mode, name, param, properties, comment);
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
        // TODO Auto-generated method stub
        return null;
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

}
