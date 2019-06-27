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
        String t = "Binary [ADD, NumConst [1], NumConst [2]]";
        Assert.assertEquals(t, add.toString());
        //checkCode(add);
    }

    /**
     * create a correct AST programmatically for a binary mod expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void modText() throws Exception {
        Expr<Void> mod = expr2AST("3*2%2^4%6");
        String t =
            "Binary [MULTIPLY, NumConst [3], Binary [MOD, NumConst [2], "
                + "Binary [MOD, FunctionExpr [MathSingleFunct [POWER, [NumConst [2], NumConst [4]]]], "
                + "NumConst [6]]]]";
        Assert.assertEquals(t, mod.toString());
        checkCode(mod);
    }

    /**
     * create a correct AST programmatically for a binary mod expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void compareText() throws Exception {
        Expr<Void> comp = expr2AST("500>=0");
        String t = "Binary [GTE, NumConst [500], NumConst [0]]";
        Assert.assertEquals(t, comp.toString());
        checkCode(comp);
    }

    /**
     * create a correct AST programmatically for a math const expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void mathConstText() throws Exception {
        Expr<Void> con = expr2AST("sin(pi)*sqrt(2)");
        String t = "Binary [MULTIPLY, FunctionExpr [MathSingleFunct [SIN, [MathConst [PI]]]], " + "FunctionExpr [MathSingleFunct [ROOT, [NumConst [2]]]]]";
        Assert.assertEquals(t, con.toString());
        checkCode(con);
    }

    /**
     * create a correct AST programmatically for a boolean expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void boolText() throws Exception {
        Expr<Void> conj = expr2AST("true&&(!false)||x==true");
        String t = "Binary [EQ, Binary [OR, Binary [AND, BoolConst [true], " + "Unary [NOT, BoolConst [false]]], Var [x]], BoolConst [true]]";
        Assert.assertEquals(t, conj.toString());
        checkCode(conj);
    }

    /**
     * create a correct AST programmatically for a string expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void strText() throws Exception {
        Expr<Void> str = expr2AST("\"String Hallo\"");
        String t = "StringConst [String Hallo]";
        Assert.assertEquals(t, str.toString());
        checkCode(str);
    }

    /**
     * create a correct AST programmatically for a color expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void colorText() throws Exception {
        Expr<Void> col = expr2AST("#F043BA");
        String t = "ColorConst [#F043BA]";
        Assert.assertEquals(t, col.toString());
        checkCode(col);
    }

    /**
     * create a correct AST programmatically for a RGB expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void rgbText() throws Exception {
        Expr<Void> rgb = expr2AST("(23,255,0,45)");
        String t = "RgbColor [NumConst [23], NumConst [255], NumConst [0], NumConst [45]]";
        Assert.assertEquals(t, rgb.toString());
        checkCode(rgb);
    }

    /**
     * create a correct AST programmatically for a connect expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void connectText() throws Exception {
        Expr<Void> connect = expr2AST("connect con1, con2");
        String t = "ConnectConst [con2]";
        Assert.assertEquals(t, connect.toString());
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
        String t = "NumConst [1], Binary [ADD, NumConst [1], NumConst [2]], " + "Unary [NEG, Binary [ADD, NumConst [1], NumConst [2]]]";
        Assert.assertEquals(t, list.toString());
    }

    /**
     * create a correct AST programmatically for a function with a list argument
     * Check that the AST is ok (but without checking the generated code).<br>
     */
    @Test
    public void average() throws Exception {
        Expr<Void> avg = expr2AST("avg([1,1+2, 10^-(1+2)])");
        String t =
            "FunctionExpr [MathSingleFunct [AVERAGE, [NumConst [1], Binary [ADD, NumConst [1], "
                + "NumConst [2]], FunctionExpr [MathSingleFunct [POW10, [Unary [NEG, Binary [ADD, NumConst [1], "
                + "NumConst [2]]]]]]]]]";
        Assert.assertEquals(t, avg.toString());
    }

    /**
     * create a correct AST programmatically for a function with a list argument
     * Check that the AST is ok (but without checking the generated code).<br>
     */
    @Test
    public void radomExp() throws Exception {
        Expr<Void> rand = expr2AST("e^randFloat()%exp(floor(randInt(1,10)))");
        String t =
            "FunctionExpr [MathSingleFunct [EXP, [Binary [MOD, FunctionExpr [MathRandomFloatFunct []], "
                + "FunctionExpr [MathSingleFunct [EXP, [FunctionExpr [MathSingleFunct [ROUNDDOWN, [FunctionExpr "
                + "[MathRandomIntFunct [[NumConst [1], NumConst [10]]]]]]]]]]]]]]";
        Assert.assertEquals(t, rand.toString());
    }

    /**
     * create a correct AST programmatically for a equality expression with math expressions.
     * Check that the AST is ok.<br>
     */
    @Test
    public void equalitytext() throws Exception {
        Expr<Void> neq = expr2AST("2==2");
        String t = "Binary [EQ, NumConst [2], NumConst [2]]";
        Assert.assertEquals(t, neq.toString());
        checkCode(neq);
    }

    /**
     * create a correct AST programmatically for a inequality expression for colors.
     * Check that the AST is ok.<br>
     */
    @Test
    public void equalityNtext() throws Exception {
        Expr<Void> neq = expr2AST("#000000 != #FFFFFF");
        String t = "Binary [NEQ, ColorConst [#000000], ColorConst [#FFFFFF]]";
        Assert.assertEquals(t, neq.toString());
        checkCode(neq);
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
