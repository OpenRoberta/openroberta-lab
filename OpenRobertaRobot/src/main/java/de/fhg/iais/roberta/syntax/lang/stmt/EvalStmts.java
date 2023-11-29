package de.fhg.iais.roberta.syntax.lang.stmt;

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
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoBasic(name = "EVAL_STMT", category = "STMT", blocklyNames = {"robActions_eval_stmt"})
public final class EvalStmts extends Stmt {
    private static final Logger LOG = LoggerFactory.getLogger(EvalStmts.class);
    private static final boolean REGENERATE_EVALBLOCK = false; // true: make eval to eval when unparsing the AST; false: make eval to blockly blocks

    public final String stmtAsString;
    public final StmtList stmtsAsBlock;
    private Integer errorCountOfSubtree = null;

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
        if ( REGENERATE_EVALBLOCK ) {
            return Jaxb2Ast.mkEvalBlockOutOfPhrase(this, this.stmtAsString);
        } else {
            if ( elevateNepoInfosAndCheckForCorrectness() ) {
                return this.stmtsAsBlock.ast2xml();
            } else {
                return Jaxb2Ast.mkEvalBlockOutOfPhrase(this, this.stmtAsString);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) throws Exception {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String stmtListAsString = Jaxb2Ast.extractField(fields, "EXPRESSION"); // TODO: EXPRESSION is a really bad name, change it!

        BlocklyType type = BlocklyType.VOID;
        BlocklyProperties properties = Jaxb2Ast.extractBlocklyProperties(block);

        final List<NepoInfo> annotations = new ArrayList<>();
        StmtList astOfStmt = EvalStmts.stmtList2AST(stmtListAsString, annotations);

        if ( astOfStmt != null ) {
            astOfStmt.setReadOnly();
            EvalStmts evalStmts = new EvalStmts(stmtListAsString, astOfStmt, type, properties);
            for ( NepoInfo info : annotations ) {
                evalStmts.addNepoInfo(info);
            }
            evalStmts.setReadOnly();
            return (Phrase) evalStmts;
        } else {
            StmtList stmtList = new StmtList();
            stmtList.setReadOnly();
            EvalStmts evalStmts = new EvalStmts(stmtListAsString, stmtList, type, properties);
            for ( NepoInfo info : annotations ) {
                evalStmts.addNepoInfo(info);
            }
            evalStmts.setReadOnly();
            return evalStmts;
        }

    }

    /**
     * Function to create an abstract syntax tree from an expression, that has been submitted as a string
     */
    private static StmtList stmtList2AST(String stmtListAsString, List<NepoInfo> annotations) throws Exception {
        ExprlyParser parser = EvalStmts.mkParser(stmtListAsString);
        EvalExprErrorListener err = new EvalExprErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(err);
        ExprlyParser.StatementListContext expression = parser.statementList();

        if ( parser.getNumberOfSyntaxErrors() > 0 ) {
            NullConst errorExpr = new NullConst(BlocklyProperties.make("EVAL", "1", expression));
            annotations.add(NepoInfo.error("PROGRAM_ERROR_EXPRBLOCK_PARSE"));
            return null;
        } else {
            TextlyVisitor<Stmt> exprlyStatements = new TextlyVisitor();
            StmtList stmtList = (StmtList) exprlyStatements.visitStatementList(expression);
            stmtList.setReadOnly();
            return stmtList;
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

