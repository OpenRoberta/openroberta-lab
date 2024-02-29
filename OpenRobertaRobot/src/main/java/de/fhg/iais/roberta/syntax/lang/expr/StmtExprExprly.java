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
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.exprEvaluator.EvalExprErrorListener;
import de.fhg.iais.roberta.exprEvaluator.TextlyVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyLexer;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.InfoCollector;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "EVAL", category = "EXPR", blocklyNames = {"robActions_eval_stmt"})
public final class StmtExprExprly extends Expr {
    private static final Logger LOG = LoggerFactory.getLogger(StmtExprExprly.class);

    public final String exprAsString;
    public final StmtList exprAsBlock;
    private Integer errorCountOfSubtree = null;

    public StmtExprExprly(String exprAsString, StmtList stmtList, BlocklyType type, BlocklyProperties properties) {
        super(properties);
        setBlocklyType(type);
        this.exprAsString = exprAsString;
        //ExprList exprList = (ExprList) exprBlock;
        //BlocklyType elementTypeOfArrayType = type.getMatchingElementTypeForArrayType();
        //this.exprAsBlock = new ListCreate(BlocklyType.VOID, stmtList, BlocklyProperties.make("robLists_create_with", "1", true, null));
        this.exprAsBlock = stmtList;
        this.setReadOnly();
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
        List<Statement> values = Jaxb2Ast.extractStatements(block, (short) 2);
        //String typeAsString = Jaxb2Ast.extractField(fields, "TYPE");

        BlocklyType type = BlocklyType.VOID;
        BlocklyProperties properties = Jaxb2Ast.extractBlocklyProperties(block);

        final List<NepoInfo> annotations = new ArrayList<>();
        StmtList astOfStmt = StmtExprExprly.expr2AST(expr, annotations);

        if ( astOfStmt != null ) {
            astOfStmt.setReadOnly();
            StmtExprExprly evalExpr = new StmtExprExprly(expr, astOfStmt, type, properties);
            for ( NepoInfo info : annotations ) {
                evalExpr.addNepoInfo(info);
            }
            return (Phrase) evalExpr;
        } else {

            StmtExprExprly evalExpr = new StmtExprExprly(expr, new StmtList(), type, properties);
            for ( NepoInfo info : annotations ) {
                evalExpr.addNepoInfo(info);
            }
            return (Phrase) evalExpr;
        }

    }

    /**
     * Function to create an abstract syntax tree from an expression, that has been submitted as a string
     */
    private static StmtList expr2AST(String exprAsString, List<NepoInfo> annotations) throws Exception {
        ExprlyParser parser = StmtExprExprly.mkParser(exprAsString);
        EvalExprErrorListener err = new EvalExprErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(err);
        ExprlyParser.StatementListContext expression = parser.statementList();

        if ( parser.getNumberOfSyntaxErrors() > 0 ) {
            NullConst errorExpr = new NullConst(BlocklyProperties.make("EVAL", "1", expression));
            annotations.add(NepoInfo.error("PROGRAM_ERROR_EXPRBLOCK_PARSE"));
            return null;
        } else {
            //ExprlyStatements exprlyStatements = new ExprlyStatements();
            TextlyVisitor<Stmt> exprlyStatements = new TextlyVisitor();
            StmtList stmtList = (StmtList) exprlyStatements.visitStatementList(expression);
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

    /**
     * retrieve typecheck errors from the AST sub-tree of this EvalExpr block.
     * They must be elevated to this block, because the EvalExpr block is explicitly designed to hide the sub-tree.
     *
     * @return the number of <b>errors</b> detected during this type check visit
     */
    public void elevateNepoInfos() {
        List<NepoInfo> infos = InfoCollector.collectInfos(this);
        for ( NepoInfo info : infos ) {
            addNepoInfo(info);
        }
    }
}

