package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.Locale;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents all unary operations from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * Client must provide operation and expression over which operation is executed. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Op, Expr, BlocklyProperties, BlocklyComment)}.<br>
 * <br>
 * The enumeration {@link Op} contains all allowed unary operations.
 */
@NepoBasic(name = "UNARY", category = "EXPR", blocklyNames = {"logic_negate"})
public final class Unary extends Expr {
    public final Op op;
    public final Expr expr;

    public Unary(Op op, Expr expr, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(op != null && expr != null && expr.isReadOnly());
        this.op = op;
        this.expr = expr;
        setReadOnly();
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
        PLUS(10, Assoc.LEFT, "+"),
        NEG(10, Assoc.LEFT, "-"),
        NOT(300, Assoc.RIGHT, "!", "not"),
        POSTFIX_INCREMENTS(1, Assoc.LEFT, "++"),
        PREFIX_INCREMENTS(1, Assoc.RIGHT, "++");

        public final String[] values;
        public final int precedence;
        public final Assoc assoc;

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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        return helper.blockToUnaryExpr(block, new ExprParam(BlocklyConstants.BOOL, BlocklyType.BOOLEAN), BlocklyConstants.NOT);
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        if ( getProperty().getBlockType().equals(BlocklyConstants.MATH_SINGLE) ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.OP, this.op.name());
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.NUM, this.expr);
        } else {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.BOOL, this.expr);
        }
        return jaxbDestination;
    }

}
