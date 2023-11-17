package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

/**
 * This class represents blockly blocks defining binary operations in the AST<br>
 * The enumeration {@link Op} specifies the allowed binary operations.
 */
@NepoBasic(name = "BINARY", category = "EXPR", blocklyNames = {"math_arithmetic", "logic_compare", "logic_operation"})
public final class Binary extends Expr {
    public final Op op;
    public final Expr left;
    public final Expr right;
    public final String operationRange;

    public Binary(Op op, Expr left, Expr right, String operationRange, BlocklyProperties properties) {
        super(properties, op.signature.returnType);
        Assert.isTrue(op != null && left != null && right != null && left.isReadOnly() && right.isReadOnly());
        this.op = op;
        this.left = left;
        this.right = right;
        this.operationRange = operationRange;
        this.setReadOnly();
    }


    /**
     * @return the expression on the right hand side. Returns subclass of {@link Expr}
     */
    public Expr getRight() {
        return this.right;
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
        return "Binary [" + this.op + ", " + this.left + ", " + this.right + "]";
    }

    /**
     * Operators for the binary expression.
     */
    public static enum Op {
        ADD(100, Assoc.LEFT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "+"),
        MINUS(100, Assoc.LEFT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "-"),
        MULTIPLY(200, Assoc.LEFT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "*"),
        DIVIDE(200, Assoc.LEFT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "/"),
        MOD(200, Assoc.NONE, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "%"),
        EQ(80, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), "=="),
        NEQ(80, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), "!=", "<>"),
        LT(90, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER), "<"),
        LTE(90, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER), "<="),
        GT(90, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER), ">"),
        GTE(90, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER), ">="),
        AND(70, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.BOOLEAN, BlocklyType.BOOLEAN), "&&", "and"),
        OR(60, Assoc.LEFT, Sig.of(BlocklyType.BOOLEAN, BlocklyType.BOOLEAN, BlocklyType.BOOLEAN), "||", "or"),
        IN(1, Assoc.LEFT, Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), ":", "in"),
        ASSIGNMENT(1, Assoc.RIGHT, Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), "="),
        ADD_ASSIGNMENT(1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "+="),
        MINUS_ASSIGNMENT(1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "-="),
        MULTIPLY_ASSIGNMENT(1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "*="),
        DIVIDE_ASSIGNMENT(1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "/="),
        MOD_ASSIGNMENT(1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "%="),
        POWER(1, Assoc.RIGHT, Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "^");

        public final String[] values;
        public final int precedence;
        public final Assoc assoc;
        public final Sig signature;

        Op(int precedence, Assoc assoc, Sig signature, String... values) {
            this.precedence = precedence;
            this.assoc = assoc;
            this.values = values;
            this.signature = signature;
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
         * get the signature. The caller has to check for <code>null</code>!
         *
         * @return the signature; if not found, return <code>null</code>
         */
        public Sig getSignature() {
            return this.signature;
        }

        /**
         * get operator from {@link Op} from string parameter. It is possible for one operator to have multiple string mappings. Throws exception if the
         * operator does not exists.
         *
         * @param name of the operator
         * @return operator from the enum {@link Op}
         */
        public static Op get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid binary operator symbol: " + s);
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
            throw new DbcException("Invalid binary operator symbol: " + s);
        }
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {

        List<Value> values;
        Phrase leftt;
        Phrase rightt;
        switch ( block.getType() ) {
            case BlocklyConstants.MATH_ARITHMETIC:
                String opp = Jaxb2Ast.extractOperation(block, BlocklyConstants.OP);
                if ( opp.equals(BlocklyConstants.POWER) ) {
                    ArrayList<ExprParam> exprParams = new ArrayList<>();
                    exprParams.add(new ExprParam(BlocklyConstants.A, BlocklyType.NUMBER_INT));
                    exprParams.add(new ExprParam(BlocklyConstants.B, BlocklyType.NUMBER_INT));
                    List<Expr> params = helper.extractExprParameters(block, exprParams);
                    return new MathPowerFunct(Jaxb2Ast.extractBlocklyProperties(block), FunctionNames.POWER, params);
                }
            default:
                return helper
                    .blockToBinaryExpr(
                        block,
                        new ExprParam(BlocklyConstants.A, BlocklyType.NUMBER_INT),
                        new ExprParam(BlocklyConstants.B, BlocklyType.NUMBER_INT),
                        BlocklyConstants.OP);

        }
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        if ( !this.operationRange.equals("") ) {
            Mutation mutation = new Mutation();
            mutation.setOperatorRange(this.operationRange);
            jaxbDestination.setMutation(mutation);
        }
        switch ( this.op ) {
            default:
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.OP, this.op.name());
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.A, this.left);
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.B, getRight());
                return jaxbDestination;
        }
    }
}
