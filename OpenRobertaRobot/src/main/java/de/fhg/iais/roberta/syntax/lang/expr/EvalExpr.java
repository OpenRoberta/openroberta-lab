package de.fhg.iais.roberta.syntax.lang.expr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.exprEvaluator.EvalExprErrorListener;
import de.fhg.iais.roberta.exprEvaluator.ExprlyVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyLexer;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser.ExpressionContext;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents blockly eval_expr block in the AST<br>
 * The user must provide the string representing the expression, This class will wrap the wanted AST instance of the expression
 */
@NepoBasic(name = "EVAL", category = "EXPR", blocklyNames = {"robActions_eval_expr"})
public final class EvalExpr extends Expr {
    private static final Logger LOG = LoggerFactory.getLogger(EvalExpr.class);

    public final String exprAsString;
    public final Expr exprAsBlock;

    public EvalExpr(String exprAsString, Expr exprBlock, BlocklyType type, BlocklyProperties properties) throws Exception {
        super(properties);
        setBlocklyType(type);
        this.exprAsString = exprAsString;
        if ( exprBlock instanceof ExprList ) {
            ExprList exprList = (ExprList) exprBlock;
            BlocklyType blocklyType;
            String blocklyName = type.getBlocklyName();
            if ( blocklyName.equals("Array_Number") ) {
                blocklyType = BlocklyType.NUMBER;
            } else if ( blocklyName.equals("Array_Boolean") ) {
                blocklyType = BlocklyType.BOOLEAN;
            } else if ( blocklyName.equals("Array_String") ) {
                blocklyType = BlocklyType.STRING;
            } else if ( blocklyName.equals("Array_Colour") ) {
                blocklyType = BlocklyType.COLOR;
            } else if ( blocklyName.equals("Array_Connection") ) {
                blocklyType = BlocklyType.CONNECTION;
            } else {
                blocklyType = BlocklyType.ANY;
            }
            this.exprAsBlock = new ListCreate(blocklyType, exprList, BlocklyProperties.make("robLists_create_with", "1", true, null));
        } else {
            this.exprAsBlock = exprBlock;
        }
        this.setReadOnly();
    }

    /**
     * factory method: create an AST instance of {@link EvalExpr}.
     *
     * @param expr representation of the expression to evaluate
     * @param type type for this expression,
     * @param properties of the block (see {@link BlocklyProperties}),
     * @param comment added from the user,
     * @return read only object representing the binary expression
     */
    public static EvalExpr make(String expr, BlocklyType type, BlocklyProperties properties) throws Exception {
        final List<NepoInfo> annotations = new ArrayList<>();
        Expr astOfExpr = EvalExpr.expr2AST(expr, annotations);
        astOfExpr.setReadOnly();
        EvalExpr evalExpr = new EvalExpr(expr, astOfExpr, type, properties);
        for ( NepoInfo nepoInfo : annotations ) {
            evalExpr.addInfo(nepoInfo);
            astOfExpr.addInfo(nepoInfo);
        }
        return evalExpr;
    }

    @Override
    public int getPrecedence() {
        return this.exprAsBlock.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.exprAsBlock.getAssoc();
    }

    @Override
    public String toString() {
        return this.exprAsBlock.toString();
    }

    @Override
    public Block ast2xml() {
        if ( true ) {
            String blocklyName = this.getBlocklyType().getBlocklyName();
            Block jaxbDestination = new Block();
            Mutation mutation = new Mutation();
            mutation.setType(blocklyName);
            Ast2Jaxb.setBasicProperties(this, jaxbDestination);
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, blocklyName);
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.EXPRESSION, this.exprAsString);
            jaxbDestination.setMutation(mutation);
            return jaxbDestination;
        } else {
            return this.exprAsBlock.ast2xml();
        }
    }

    @SuppressWarnings("unchecked")
    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) throws Exception {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String expr = Jaxb2Ast.extractField(fields, "EXPRESSION");
        String typeAsString = Jaxb2Ast.extractField(fields, "TYPE");
        return (Phrase) EvalExpr.make(expr, BlocklyType.getByBlocklyName(typeAsString), Jaxb2Ast.extractBlocklyProperties(block));

    }

    /**
     * Function to create an abstract syntax tree from an expression, that has been submitted as a string
     */
    private static Expr expr2AST(String expr, List<NepoInfo> annotations) throws Exception {
        ExprlyParser parser = EvalExpr.mkParser(expr);
        EvalExprErrorListener err = new EvalExprErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(err);
        ExpressionContext expression = parser.expression();
        if ( parser.getNumberOfSyntaxErrors() > 0 || !parser.isMatchedEOF() ) {
            for ( String s : err.getError() ) {
                LOG.error(s);
            }
            NullConst errorExpr = new NullConst(BlocklyProperties.make("EVAL", "1", null));
            annotations.add(NepoInfo.error("PROGRAM_ERROR_EXPRBLOCK_PARSE"));
            return errorExpr;
        } else {
            ExprlyVisitor eval = new ExprlyVisitor();
            Expr blk = eval.visitExpression(expression);
            return blk;
        }
    }

    /**
     * Function to create the parser for the expression
     */
    private static ExprlyParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        CharStream input = CharStreams.fromStream(inputStream);
        ExprlyLexer lexer = new ExprlyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprlyParser parser = new ExprlyParser(tokens);
        return parser;
    }
}
