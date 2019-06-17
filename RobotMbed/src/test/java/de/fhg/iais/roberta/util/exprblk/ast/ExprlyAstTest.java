package de.fhg.iais.roberta.util.exprblk.ast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
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
     * create a correct AST programmatically for a binary math expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void test1add2text() throws Exception {
        Expr<Void> add = expr2AST("(((1)+2))");
        checkCode(add);
    }

    /**
     * create a correct AST programmatically for a binary mod expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void modText() throws Exception {
        Expr<Void> mod = expr2AST("3*2%2");
        checkCode(mod);
    }

    /**
     * create a correct AST programmatically for a binary mod expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void compareText() throws Exception {
        Expr<Void> comp = expr2AST("500>=0");
        checkCode(comp);
    }

    /**
     * create a correct AST programmatically for a math const expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void mathConstText() throws Exception {
        Expr<Void> con = expr2AST("pi");
        checkCode(con);
    }

    /**
     * create a correct AST programmatically for a boolean expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void boolText() throws Exception {
        Expr<Void> conj = expr2AST("True&&(!falSe)||FALSE==true");
        checkCode(conj);
    }

    /**
     * create a correct AST programmatically for a string expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void strText() throws Exception {
        Expr<Void> str = expr2AST("String:Hallo");
        checkCode(str);
    }

    /**
     * create a correct AST programmatically for a color expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void colorText() throws Exception {
        Expr<Void> col = expr2AST("#F043BA");
        checkCode(col);
    }

    /**
     * create a correct AST programmatically for a RGB expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void rgbText() throws Exception {
        Expr<Void> rgb = expr2AST("(23,255,0,45)");
        checkCode(rgb);
    }

    /**
     * create a correct AST programmatically for a connect expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void connectText() throws Exception {
        Expr<Void> connect = expr2AST("Connection: con1,con2");
        checkCode(connect);
    }

    /**
     * create a correct AST programmatically for a list expression (we use the math expression list
     * for the test).
     * Check that the AST is ok (but without checking the generated code).<br>
     */
    @Test
    public void listmText() throws Exception {
        Expr<Void> list = expr2AST("[1,1+2,-(1+2)]");
        String t = "NumConst [1], Binary [ADD, NumConst [1], NumConst [2]], Unary [NEG, Binary [ADD, NumConst [1], NumConst [2]]]";
        Assert.assertEquals(t, list.toString());
    }

    /**
     * create a correct AST programmatically for a equality expression with math expressions.
     * Check that the AST is ok.<br>
     */
    @Test
    public void equalitytext() throws Exception {
        Expr<Void> neq = expr2AST("2==2");
        checkCode(neq);
    }

    /**
     * create a correct AST programmatically for a inequality expression for colors.
     * Check that the AST is ok.<br>
     */
    @Test
    public void equalityNtext() throws Exception {
        Expr<Void> neq = expr2AST("#000000 != #FFFFFF");
        checkCode(neq);
    }

    /**
     * create a correct AST programmatically for a assignment expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void assignText() throws Exception {
        Expr<Void> assign = expr2AST("x:= (24,45,46,255)");
        checkCode(assign);
    }

    /**
     * create a correct AST programmatically for a assignment expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void assign1Text() throws Exception {
        // Parentheses are required in these assignment statements
        Expr<Void> assign = expr2AST("x:= (25+3*7)");
        checkCode(assign);
    }

    /**
     * function to check the generated code with the ast
     */
    private void checkCode(Expr<Void> expr) {
        ArrayList<Phrase<Void>> addInList = new ArrayList<>();
        addInList.add(expr);
        ArrayList<ArrayList<Phrase<Void>>> addInListInList = new ArrayList<>();
        addInListInList.add(addInList);
        CalliopeCppVisitor cppVisitor = new CalliopeCppVisitor(addInListInList);
        cppVisitor.visitExprStmt(ExprStmt.make(expr));
        LOG.info("generated C++ code: " + cppVisitor.getSb().toString());
        MicrobitPythonVisitor pythonVisitor = new MicrobitPythonVisitor(addInListInList);
        pythonVisitor.visitExprStmt(ExprStmt.make(expr));
        LOG.info("generated Python code: " + pythonVisitor.getSb().toString());
    }

    /**
     * Function to create ast
     */
    private Expr<Void> expr2AST(String expr) throws Exception {
        ExprlyParser parser = mkParser(expr);
        ExprlyAST eval = new ExprlyAST();
        ExpressionContext expression = parser.expression();
        Expr<Void> block = eval.visitExpression(expression);
        return block;
    }

    /**
     * Function to create the parser for the expression
     */
    private ExprlyParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        ExprlyLexer lexer = new ExprlyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprlyParser parser = new ExprlyParser(tokens);
        return parser;
    }

}
