package de.fhg.iais.roberta.syntax.lang.expr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.fhg.iais.roberta.syntax.lang.expr.eval.EvalExprErrorListener;
import de.fhg.iais.roberta.syntax.lang.expr.eval.ExprlyVisitor;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents blockly eval_expr block in the AST<br>
 * The user must provide the string representing the expression, This class will wrap the wanted AST instance of the expression
 */
public class EvalExpr<V> extends Expr<V> {
    private final String expr;
    private final String type;
    private static final Logger LOG = LoggerFactory.getLogger(EvalExpr.class);
    private final Expr<V> exprBlock;

    private EvalExpr(String expr, Expr<V> exprBlock, String type, BlocklyBlockProperties properties, BlocklyComment comment) throws Exception {
        super(exprBlock.getKind(), properties, comment);
        this.expr = expr;
        this.type = type;
        if ( exprBlock instanceof ExprList<?> ) {
            ExprList<V> exprList = (ExprList<V>) exprBlock;
            BlocklyType blocklyType;
            if ( this.type.equals("Array_Number") ) {
                blocklyType = BlocklyType.NUMBER;
            } else if ( this.type.equals("Array_Boolean") ) {
                blocklyType = BlocklyType.BOOLEAN;
            } else if ( this.type.equals("Array_String") ) {
                blocklyType = BlocklyType.STRING;
            } else if ( this.type.equals("Array_Colour") ) {
                blocklyType = BlocklyType.COLOR;
            } else if ( this.type.equals("Array_Connection") ) {
                blocklyType = BlocklyType.CONNECTION;
            } else {
                blocklyType = BlocklyType.ANY;
            }
            this.exprBlock = ListCreate.make(blocklyType, exprList, properties, comment);
        } else {
            this.exprBlock = exprBlock;
        }
        this.setReadOnly();
    }

    /**
     * factory method: create an AST instance of {@link EvalExpr}.
     *
     * @param textual representation of the expression to evaluate
     * @param expected type for this expression,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object representing the binary expression
     */
    public static <V> EvalExpr<V> make(String expr, String type, BlocklyBlockProperties properties, BlocklyComment comment) throws Exception {
        final List<NepoInfo> annotations = new ArrayList<>();
        Expr<V> astOfExpr = expr2AST(expr, annotations);
        astOfExpr.setReadOnly();
        EvalExpr<V> evalExpr = new EvalExpr<>(expr, astOfExpr, type, properties, comment);
        for ( NepoInfo nepoInfo : annotations ) {
            evalExpr.addInfo(nepoInfo);
            astOfExpr.addInfo(nepoInfo);
        }
        return evalExpr;
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitEvalExpr(this);
    }

    @Override
    public String toString() {
        return this.exprBlock.toString();
    }

    /**
     * @return AST instance of the expression
     */
    public Expr<V> getValue() {
        return this.exprBlock;
    }

    /**
     * @return AST instance of the expression
     */
    public Expr<V> getExpr() {
        return this.exprBlock;
    }

    /**
     * @return expected type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return expression string
     */
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

    /**
     * Function to create an abstract syntax tree from an expression, that has been submitted as a string
     */
    private static <V> Expr<V> expr2AST(String expr, List<NepoInfo> annotations) throws Exception {
        ExprlyParser parser = mkParser(expr);
        EvalExprErrorListener err = new EvalExprErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(err);
        ExpressionContext expression = parser.expression();
        if ( parser.getNumberOfSyntaxErrors() > 0 || !parser.isMatchedEOF() ) {
            for ( String s : err.getError() ) {
                LOG.error(s);
            }
            NullConst<V> errorExpr = NullConst.make(BlocklyBlockProperties.make("1", "1"), null);
            annotations.add(NepoInfo.error("PROGRAM_ERROR_EXPRBLOCK_PARSE"));
            return errorExpr;
        } else {
            ExprlyVisitor<V> eval = new ExprlyVisitor<>();
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
}
