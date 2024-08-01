package de.fhg.iais.roberta.syntax.lang.expr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.exprEvaluator.EvalExprStmtErrorListener;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.textly.generated.TextlyJavaLexer;
import de.fhg.iais.roberta.textly.generated.TextlyJavaParser;
import de.fhg.iais.roberta.textly.generated.TextlyJavaParser.ExpressionContext;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents blockly eval_expr block in the AST<br>
 * The user must provide the string representing the expression, This class will wrap the wanted AST instance of the expression
 */
@NepoBasic(name = "EVAL_EXPR", category = "EXPR", blocklyNames = {"robActions_eval_expr"})
public final class EvalExpr extends Expr {
    private static final Logger LOG = LoggerFactory.getLogger(EvalExpr.class);
    public final String exprAsString;
    public final Expr exprAsBlock;


    public EvalExpr(String exprAsString, Expr exprBlock, BlocklyType type, BlocklyProperties properties) {
        super(properties);
        setBlocklyType(type);
        this.exprAsString = exprAsString;
        if ( exprBlock instanceof ExprList ) {
            // TODO: refactor, should be removed (if possible?), simplified 27.11.23 rb
            ExprList exprList = (ExprList) exprBlock;
            BlocklyType elementTypeOfArrayType = type.getMatchingElementTypeForArrayType();
            this.exprAsBlock = new ListCreate(elementTypeOfArrayType, exprList, BlocklyProperties.make("robLists_create_with", "1", true, null));
        } else {
            this.exprAsBlock = exprBlock;
        }
        this.setReadOnly();
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
    public List<Block> ast2xml() {
        return Jaxb2Ast.mkEvalBlockOutOfPhrase(this, this.exprAsString, BlocklyConstants.EXPRESSION);
    }

    @SuppressWarnings("unchecked")
    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) throws Exception {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String expr = Jaxb2Ast.extractField(fields, "EXPRESSION");
        String typeAsString = Jaxb2Ast.extractField(fields, "TYPE");
        BlocklyType type = BlocklyType.getByBlocklyName(typeAsString);
        BlocklyProperties properties = Jaxb2Ast.extractBlocklyProperties(block);

        String pluginProperties = helper.getRobotFactory().getPluginProperties().getPluginProperties().getProperty("robot.class.textlyJava");

        if ( pluginProperties != null ) {
            Class<?> pluginClass = Class.forName(pluginProperties);
            Expr astOfExpr = EvalExpr.expr2AST(expr, pluginClass);
            astOfExpr.setReadOnly();
            EvalExpr evalExpr = new EvalExpr(expr, astOfExpr, type, properties);
            return evalExpr;

        } else {
            Expr result = new EmptyExpr(BlocklyType.NOTHING);
            result.addTextlyError("This robot does not support TextlyJava", false);
            EvalExpr evalExpr = new EvalExpr(expr, result, type, properties);
            return evalExpr;
        }
    }

    /**
     * Function to create an abstract syntax tree from an expression, that has been submitted as a string
     */
    private static Expr expr2AST(String exprAsString, Class<?> pluginClass) throws Exception {
        TextlyJavaParser parser = EvalExpr.mkParser(exprAsString);
        EvalExprStmtErrorListener err = new EvalExprStmtErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(err);
        ExpressionContext expression = parser.expression();
        if ( parser.getNumberOfSyntaxErrors() > 0 || !parser.isMatchedEOF() ) {
            NullConst errorExpr = new NullConst(BlocklyProperties.make("EVAL", "1", expression));
            String errorMessage = err.getError().toString();
            errorExpr.addTextlyError(errorMessage, false);
            return errorExpr;
        } else {
            Object textlyJavaVisitorInstance = pluginClass.getDeclaredConstructor().newInstance();
            Method visitExpressionMethod = pluginClass.getMethod("visitExpression", ExpressionContext.class);
            Expr expr = (Expr) visitExpressionMethod.invoke(textlyJavaVisitorInstance, expression);
            return expr;
        }
    }

    /**
     * Function to create the parser for the expression
     */
    private static TextlyJavaParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        CharStream input = CharStreams.fromStream(inputStream);
        TextlyJavaLexer lexer = new TextlyJavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TextlyJavaParser parser = new TextlyJavaParser(tokens);
        return parser;
    }
}
