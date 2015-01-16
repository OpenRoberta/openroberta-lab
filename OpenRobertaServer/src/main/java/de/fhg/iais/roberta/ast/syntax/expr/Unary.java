package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

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
        super(Phrase.Kind.UNARY, properties, comment);
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
    public String toString() {
        return "Unary [" + this.op + ", " + this.expr + "]";
    }

    /**
     * Operators for the unary expression.
     */
    public static enum Op {
        PLUS( 10, Assoc.LEFT, "+" ), NEG( 10, Assoc.LEFT, "-" ), NOT( 300, Assoc.RIGHT, "!" ), POSTFIX_INCREMENTS( 1, Assoc.LEFT, "++" ), REFIX_INCREMENTS(
            1,
            Assoc.RIGHT,
            "++" );

        private final String[] values;
        private final int precedence;
        private final Assoc assoc;

        private Op(int precedence, Assoc assoc, String... values) {
            this.precedence = precedence;
            this.assoc = assoc;
            this.values = values;
        }

        /**
         * @return mathematical symbol of the operation.
         */
        public String getOpSymbol() {
            if ( this.values.length == 0 ) {
                return this.toString();
            } else {
                return this.values[0];
            }
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
         * get operator from {@link Op} from string parameter. It is possible for one operator to have multiple string mappings.
         * Throws exception if the operator does not exists.
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitUnary(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        if ( getProperty().getBlockType().equals("math_single") ) {
            AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.OP_, getOp().name());
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.NUM, getExpr());
        } else {
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.BOOL, getExpr());
        }
        return jaxbDestination;
    }

}
