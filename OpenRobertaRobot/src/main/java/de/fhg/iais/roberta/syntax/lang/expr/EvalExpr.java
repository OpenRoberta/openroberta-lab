package de.fhg.iais.roberta.syntax.lang.expr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.exprly.generated.ExprlyLexer;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser.ExpressionContext;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.eval.resources.ExprlyAST;
import de.fhg.iais.roberta.syntax.lang.expr.eval.resources.ExprlyTypechecker;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public class EvalExpr<V> extends Expr<V> {
    private final String expr, type;
    private final Expr<V> exprBlock;
    private final ExprlyTypechecker<V> checker;
    private boolean syntaxError = false;

    private EvalExpr(String expr, String type, BlocklyBlockProperties properties, BlocklyComment comment) throws Exception {
        super(expr2AST(expr).getKind(), properties, comment);
        Expr<V> exprBlk = expr2AST(expr);
        if ( exprBlk instanceof NullConst<?> ) {
            this.syntaxError = true;
        }
        this.expr = expr;
        this.type = type;
        if ( exprBlk instanceof ExprList<?> ) {
            exprBlk.setReadOnly();
            if ( this.type.equals("Array_Number") ) {
                this.exprBlock = ListCreate.make(BlocklyType.NUMBER, (ExprList<V>) exprBlk);
            } else if ( this.type.equals("Array_Boolean") ) {
                this.exprBlock = ListCreate.make(BlocklyType.BOOLEAN, (ExprList<V>) exprBlk);
            } else if ( this.type.equals("Array_String") ) {
                this.exprBlock = ListCreate.make(BlocklyType.STRING, (ExprList<V>) exprBlk);
            } else if ( this.type.equals("Array_Colour") ) {
                this.exprBlock = ListCreate.make(BlocklyType.COLOR, (ExprList<V>) exprBlk);
            } else if ( this.type.equals("Array_Connection") ) {
                this.exprBlock = ListCreate.make(BlocklyType.CONNECTION, (ExprList<V>) exprBlk);
            } else {
                throw new IllegalArgumentException("Invalid type for EvalExpr");
            }
        } else {
            this.exprBlock = exprBlk;
        }
        this.checker = new ExprlyTypechecker<>(this.exprBlock, BlocklyType.get(this.type));

        this.setReadOnly();
    }

    public static <V> EvalExpr<V> make(String expr, String type, BlocklyBlockProperties properties, BlocklyComment comment) throws Exception {
        return new EvalExpr<V>(expr, type, properties, comment);
    }

    @SuppressWarnings("unchecked")
    public static <V> EvalExpr<V> make(String expr, String type) throws Exception {
        return new EvalExpr<V>(expr, type, BlocklyBlockProperties.make("1", "1"), null);
    }

    @Override
    public int getPrecedence() {
        return this.exprBlock.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.exprBlock.getAssoc();
    }

    @Override
    public BlocklyType getVarType() {
        return this.exprBlock.getVarType();
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitEvalExpr(this);
    }

    @Override
    public String toString() {
        return this.exprBlock.toString();
    }

    public Expr<V> getValue() {
        return this.exprBlock;
    }

    public Expr<V> getExpr() {
        return this.exprBlock;
    }

    public String getType() {
        return this.type;
    }

    public String getExprStr() {
        return this.expr;
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Mutation mutation = new Mutation();
        mutation.setType(this.getType());
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.TYPE, this.getType());
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.EXPRESSION, this.getExprStr());
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }

    /**
     * Function to create ast
     *
     * @param <V>
     */
    @SuppressWarnings({
        "hiding",
        "rawtypes",
        "unchecked"
    })
    private static <V> Expr<V> expr2AST(String expr) throws Exception {
        ExprlyParser parser = mkParser(expr);
        ExprlyAST<V> eval = new ExprlyAST<V>();
        ExpressionContext expression = parser.expression();
        if ( parser.getNumberOfSyntaxErrors() > 0 ) {
            try {
                Expr<V> blk = eval.visitExpression(expression);
                return blk;
            } catch ( Exception e ) {
                return NullConst.make();
            }
        } else {
            Expr<V> blk = eval.visitExpression(expression);
            return blk;
        }

    }

    /**
     * Function to create the parser for the expression
     */
    private static ExprlyParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        ExprlyLexer lexer = new ExprlyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprlyParser parser = new ExprlyParser(tokens);
        return parser;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) throws Exception {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String expr = helper.extractField(fields, "EXPRESSION");
        String type = helper.extractField(fields, "TYPE");
        return (Phrase<V>) EvalExpr.make(expr, type, helper.extractBlockProperties(block), helper.extractComment(block));

    }

}
