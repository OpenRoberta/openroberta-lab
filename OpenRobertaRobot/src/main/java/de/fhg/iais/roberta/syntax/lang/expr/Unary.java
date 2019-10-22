package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.Locale;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents all unary operations from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * Client must provide operation and expression over which operation is executed. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Op, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * <br>
 * The enumeration {@link Op} contains all allowed unary operations.
 */
public class Unary<V> extends Expr<V> {
    private final Op op;
    private final Expr<V> expr;

    private Unary(Op op, Expr<V> expr, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("UNARY"), properties, comment);
        Assert.isTrue(op != null && expr != null && expr.isReadOnly());
        this.op = op;
        this.expr = expr;
        setReadOnly();
    }

    /**
     * creates instance of {@link Unary}. This instance is read only and can not be modified.
     *
     * @param op operator; ; must be <b>not</b> null,
     * @param expr expression over which the operation is performed; must be <b>not</b> null and <b>read only</b>,,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Unary}
     */
    public static <V> Unary<V> make(Op op, Expr<V> expr, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Unary<V>(op, expr, properties, comment);
    }

    /**
     * @return the operation in the binary expression. See enum {@link Op} for all possible operations
     */
    public Op getOp() {
        return this.op;
    }

    /**
     * @return the expression on which the operation is performed. Returns subclass of {@link Expr}
     */
    public Expr<V> getExpr() {
        return this.expr;
    }

    @Override
    public int getPrecedence() {
        return this.op.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.op.getAssoc();
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.CAPTURED_TYPE;
    }

    @Override
    public String toString() {
        return "Unary [" + this.op + ", " + this.expr + "]";
    }

    /**
     * Operators for the unary expression.
     */
    public static enum Op {
        PLUS( 10, Assoc.LEFT, "+" ),
        NEG( 10, Assoc.LEFT, "-" ),
        NOT( 300, Assoc.RIGHT, "!", "not" ),
        POSTFIX_INCREMENTS( 1, Assoc.LEFT, "++" ),
        PREFIX_INCREMENTS( 1, Assoc.RIGHT, "++" );

        private final String[] values;
        private final int precedence;
        private final Assoc assoc;

        private Op(int precedence, Assoc assoc, String... values) {
            this.precedence = precedence;
            this.assoc = assoc;
            this.values = values;
        }

        /**
         * @return precedence of the operator.
         */
        public int getPrecedence() {
            return this.precedence;
        }

        /**
         * @return association of the operator
         */
        public Assoc getAssoc() {
            return this.assoc;
        }

        /**
         * get operator from {@link Op} from string parameter. It is possible for one operator to have multiple string mappings. Throws exception if the
         * operator does not exists.
         *
         * @param name of the operator
         * @return operator from the enum {@link Op}, never null
         */
        public static Op get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid unary operator symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Op op : Op.values() ) {
                if ( op.toString().equals(sUpper) ) {
                    return op;
                }
                for ( String value : op.values ) {
                    if ( sUpper.equals(value) ) {
                        return op;
                    }
                }
            }
            throw new DbcException("Invalid unary operator symbol: " + s);
        }
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitUnary(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return helper.blockToUnaryExpr(block, new ExprParam(BlocklyConstants.BOOL, BlocklyType.BOOLEAN), BlocklyConstants.NOT);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        if ( getProperty().getBlockType().equals(BlocklyConstants.MATH_SINGLE) ) {
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.OP, getOp().name());
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.NUM, getExpr());
        } else {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.BOOL, getExpr());
        }
        return jaxbDestination;
    }

}
