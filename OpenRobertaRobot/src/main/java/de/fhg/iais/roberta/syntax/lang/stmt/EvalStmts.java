package de.fhg.iais.roberta.syntax.lang.stmt;

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
import de.fhg.iais.roberta.exprEvaluator.BlockEvalStmtErrorListener;
import de.fhg.iais.roberta.exprEvaluator.EvalExprStmtErrorListener;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.textly.generated.TextlyJavaLexer;
import de.fhg.iais.roberta.textly.generated.TextlyJavaParser;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "EVAL_STMT", category = "STMT", blocklyNames = {"robActions_eval_stmt"})
public final class EvalStmts extends Stmt {
    private static final Logger LOG = LoggerFactory.getLogger(EvalStmts.class);
    public final String stmtAsString;
    public final StmtList stmtsAsBlock;

    public EvalStmts(String stmtAsString, StmtList stmtList, BlocklyType type, BlocklyProperties properties) {
        super(properties);
        setBlocklyType(type);
        this.stmtAsString = stmtAsString;
        this.stmtsAsBlock = stmtList;
        this.setReadOnly();
    }

    @Override
    public String toString() {
        return this.stmtsAsBlock.toString();
    }

    @Override
    public List<Block> ast2xml() {
        return Jaxb2Ast.mkEvalBlockOutOfPhrase(this, this.stmtAsString, BlocklyConstants.STMTEXPRESSION);
    }

    @SuppressWarnings("unchecked")
    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) throws Exception {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String stmtListAsString = Jaxb2Ast.extractField(fields, "STMTEXPRESSION");

        BlocklyType type = BlocklyType.VOID;
        BlocklyProperties properties = Jaxb2Ast.extractBlocklyProperties(block);

        String pluginProperties = helper.getRobotFactory().getPluginProperties().getPluginProperties().getProperty("robot.class.textlyJava");

        if ( pluginProperties != null ) {
            Class<?> pluginClass = Class.forName(pluginProperties);
            StmtList astOfStmt = EvalStmts.stmtList2AST(stmtListAsString, pluginClass);
            if ( astOfStmt != null ) {
                astOfStmt.setReadOnly();
                EvalStmts evalStmts = new EvalStmts(stmtListAsString, astOfStmt, type, properties);
                evalStmts.setReadOnly();
                return (Phrase) evalStmts;
            } else {
                StmtList stmtList = new StmtList();
                stmtList.setReadOnly();
                EvalStmts evalStmts = new EvalStmts(stmtListAsString, stmtList, type, properties);
                evalStmts.setReadOnly();
                return evalStmts;
            }
        } else {
            StmtList statementList = new StmtList();
            statementList.setReadOnly();
            statementList.addTextlyError("This robot does not support Textly", true);
            EvalStmts evalStmts = new EvalStmts(stmtListAsString, statementList, type, properties);
            evalStmts.setReadOnly();

            return evalStmts;
        }
    }

    /**
     * Function to create an abstract syntax tree from a statement expression, that has been submitted as a string
     */
    private static StmtList stmtList2AST(String stmtListAsString, Class<?> pluginClass) throws Exception {
        TextlyJavaParser parser = EvalStmts.mkParser(stmtListAsString);
        BlockEvalStmtErrorListener err = new BlockEvalStmtErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(err);
        TextlyJavaParser.StatementListContext statementListExpr = parser.statementList();

        if ( parser.getNumberOfSyntaxErrors() > 0 ) {
            StmtList statementList = new StmtList();
            statementList.setReadOnly();
            statementList.addTextlyError(err.getError().toString(), false);
            return statementList;
        } else {
            Object textlyJavaVisitorInstance = pluginClass.getDeclaredConstructor().newInstance();
            Method visitStatementListMethod = pluginClass.getMethod("visitStatementList", TextlyJavaParser.StatementListContext.class);
            StmtList stmtList = (StmtList) visitStatementListMethod.invoke(textlyJavaVisitorInstance, statementListExpr);
            stmtList.setReadOnly();
            return stmtList;
        }
    }

    /**
     * Function to create the parser for the Statement expression
     */
    private static TextlyJavaParser mkParser(String stmtExpr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(stmtExpr.getBytes("UTF-8"));
        CharStream input = CharStreams.fromStream(inputStream);
        TextlyJavaLexer lexer = new TextlyJavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TextlyJavaParser parser = new TextlyJavaParser(tokens);
        return parser;
    }
}

