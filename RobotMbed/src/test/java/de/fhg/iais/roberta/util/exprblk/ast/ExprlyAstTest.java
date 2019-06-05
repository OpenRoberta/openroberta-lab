package de.fhg.iais.roberta.util.exprblk.ast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.exprly.generated.ExprlyLexer;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser.ExpressionContext;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.CalliopeCppVisitor;
import de.fhg.iais.roberta.visitor.codegen.MicrobitPythonVisitor;

public class ExprlyAstTest {
    private static final Logger LOG = LoggerFactory.getLogger(ExprlyAstTest.class);
    private final GenericHelperForXmlTest h = new GenericHelperForXmlTest();

    /**
     * create a correct AST programmatically. Check that the AST is ok.<br>
     */
    @Test
    public void test1add2text() throws Exception {
        Expr<Void> add = expr2AST("(((1)+2) || (3+4))");
        //        TypecheckVisitor tcAdd = TypecheckVisitor.makeVisitorAndTypecheck(add);
        //        Assert.assertEquals(0, tcAdd.getErrorCount());
        ArrayList<Phrase<Void>> addInList = new ArrayList<>();
        addInList.add(add);
        ArrayList<ArrayList<Phrase<Void>>> addInListInList = new ArrayList<>();
        addInListInList.add(addInList);
        CalliopeCppVisitor cppVisitor = new CalliopeCppVisitor(addInListInList);
        cppVisitor.visitExprStmt(ExprStmt.make(add));
        LOG.info("generated C++ code: " + cppVisitor.getSb().toString());
        MicrobitPythonVisitor pythonVisitor = new MicrobitPythonVisitor(addInListInList);
        pythonVisitor.visitExprStmt(ExprStmt.make(add));
        LOG.info("generated Python code: " + pythonVisitor.getSb().toString());
    }

    private Expr<Void> expr2AST(String expr) throws Exception {
        ExprlyParser parser = mkParser(expr);
        ExprlyAST eval = new ExprlyAST();
        ExpressionContext expression = parser.expression();
        Expr<Void> block = eval.visitExpression(expression);
        return block;
    }

    private ExprlyParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        ExprlyLexer lexer = new ExprlyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprlyParser parser = new ExprlyParser(tokens);
        return parser;
    }

}
