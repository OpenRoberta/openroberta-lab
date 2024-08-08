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
import de.fhg.iais.roberta.exprEvaluator.EvalExprErrorListener;
import de.fhg.iais.roberta.exprEvaluator.TextlyVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyLexer;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser.ExpressionContext;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * This class represents blockly eval_expr block in the AST<br>
 * The user must provide the string representing the expression, This class will wrap the wanted AST instance of the expression
 */
@NepoBasic(name = "EVAL_EXPR", category = "EXPR", blocklyNames = {"robActions_eval_expr"})
public final class EvalExpr extends Expr {
    private static final Logger LOG = LoggerFactory.getLogger(EvalExpr.class);
    private static final boolean REGENERATE_EVALBLOCK = true; // true: make eval to eval when unparsing the AST; false: make eval to blockly blocks

    public final String exprAsString;
    public final Expr exprAsBlock;
    private Integer errorCountOfSubtree = null;

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
        if ( REGENERATE_EVALBLOCK ) {
            return Jaxb2Ast.mkEvalBlockOutOfPhrase(this, this.exprAsString);
        } else {
            if ( elevateNepoInfosAndCheckForCorrectness() ) {
                return this.exprAsBlock.ast2xml();
            } else {
                return Jaxb2Ast.mkEvalBlockOutOfPhrase(this, this.exprAsString);
            }
        }
    }


    @SuppressWarnings("unchecked")
    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) throws Exception {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String expr = Jaxb2Ast.extractField(fields, "EXPRESSION");
        String typeAsString = Jaxb2Ast.extractField(fields, "TYPE");
        BlocklyType type = BlocklyType.getByBlocklyName(typeAsString);
        BlocklyProperties properties = Jaxb2Ast.extractBlocklyProperties(block);

        final List<NepoInfo> annotations = new ArrayList<>();
        Expr astOfExpr = EvalExpr.expr2AST(expr, annotations);
        astOfExpr.setReadOnly();

        EvalExpr evalExpr = new EvalExpr(expr, astOfExpr, type, properties);
        for ( NepoInfo info : annotations ) {
            evalExpr.addNepoInfo(info);
        }
        return evalExpr;
    }

    /**
     * Function to create an abstract syntax tree from an expression, that has been submitted as a string
     */
    private static Expr expr2AST(String exprAsString, List<NepoInfo> annotations) throws Exception {
        ExprlyParser parser = EvalExpr.mkParser(exprAsString);
        EvalExprErrorListener err = new EvalExprErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(err);
        ExpressionContext expression = parser.expression();
        if ( parser.getNumberOfSyntaxErrors() > 0 || !parser.isMatchedEOF() ) {
            NullConst errorExpr = new NullConst(BlocklyProperties.make("EVAL", "1", expression));
            annotations.add(NepoInfo.error("PROGRAM_ERROR_EXPRBLOCK_PARSE"));
            return errorExpr;
        } else {
            TextlyVisitor<Expr> exprlyVisitor = new TextlyVisitor();
            Expr expr = exprlyVisitor.visitExpression(expression);
            return expr;
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
