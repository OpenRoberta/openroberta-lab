package de.fhg.iais.roberta.util.exprblk.ast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import de.fhg.iais.roberta.syntax.lang.expr.eval.resources.ExprlyAST;
import de.fhg.iais.roberta.syntax.lang.expr.eval.resources.ExprlyTypechecker;
import de.fhg.iais.roberta.syntax.lang.expr.eval.resources.ExprlyUnParser;
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
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(add);
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(add);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, add.toString());
        Assert.assertEquals(add.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a binary mod expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void modText() throws Exception {
        Expr<Void> mod = expr2AST("3*2%2^4%6");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(mod);
        String t =
            "Binary [MULTIPLY, NumConst [3], Binary [MOD, NumConst [2], Binary [MOD, MathPowerFunct [POWER, [NumConst [2], NumConst [4]]], NumConst [6]]]]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(mod);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, mod.toString());
        Assert.assertEquals(mod.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a binary mod expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void compareText() throws Exception {
        Expr<Void> comp = expr2AST("500>=0");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(comp);
        String t = "Binary [GTE, NumConst [500], NumConst [0]]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(comp);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, comp.toString());
        Assert.assertEquals(comp.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a math const expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void mathConstText() throws Exception {
        Expr<Void> con = expr2AST("sin(pi)*sqrt(2)");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(con);
        String t = "Binary [MULTIPLY, FunctionExpr [MathSingleFunct [SIN, [MathConst [PI]]]], " + "FunctionExpr [MathSingleFunct [ROOT, [NumConst [2]]]]]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(con);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, con.toString());
        Assert.assertEquals(con.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a boolean expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void boolText() throws Exception {
        Expr<Void> conj = expr2AST("true&&(!false)||x==true");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(conj);
        String t = "Binary [EQ, Binary [OR, Binary [AND, BoolConst [true], " + "Unary [NOT, BoolConst [false]]], Var [x]], BoolConst [true]]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(conj);
        // Here the number of errors is 1 since the type of Var types is VOID by default and a bool is expected.
        Assert.assertTrue(1 == c.check());
        Assert.assertEquals(t, conj.toString());
        Assert.assertEquals(conj.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a string expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void strText() throws Exception {
        Expr<Void> str = expr2AST("\"String Hallo\"");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(str);
        String t = "StringConst [String Hallo]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(str);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, str.toString());
        Assert.assertEquals(str.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a color expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void colorText() throws Exception {
        Expr<Void> col = expr2AST("#F043BA");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(col);
        String t = "ColorConst [#F043BA]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(col);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, col.toString());
        Assert.assertEquals(col.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a RGB expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void rgbText() throws Exception {
        Expr<Void> rgb = expr2AST("(23,255,0,45)");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(rgb);
        String t = "RgbColor [NumConst [23], NumConst [255], NumConst [0], NumConst [45]]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(rgb);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, rgb.toString());
        Assert.assertEquals(rgb.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a connect expression.
     * Check that the AST is ok.<br>
     */
    @Test
    public void connectText() throws Exception {
        Expr<Void> connect = expr2AST("connect con1, con2");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(connect);
        String t = "ConnectConst [con2]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(connect);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, connect.toString());
        Assert.assertEquals(connect.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a list expression (we use the math expression list
     * for the test).
     * Check that the AST is ok (but without checking the generated code).<br>
     */
    @Test
    public void listmText() throws Exception {
        Expr<Void> list = expr2AST("([1,1+2,-(1+2)])");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(list);
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(list);
        Assert.assertTrue(0 == c.check());
        String t = "NumConst [1], Binary [ADD, NumConst [1], NumConst [2]], " + "Unary [NEG, Binary [ADD, NumConst [1], NumConst [2]]]";
        Assert.assertEquals(t, list.toString());
        Assert.assertEquals(list.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a function with a list argument
     * Check that the AST is ok (but without checking the generated code).<br>
     */
    @Test
    public void average() throws Exception {
        Expr<Void> avg = expr2AST("avg([1,1+2, 10^-(1+2)])");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(avg);
        String t =
            "FunctionExpr [MathOnListFunct [AVERAGE, [NumConst [1], Binary [ADD, NumConst [1], NumConst [2]], MathPowerFunct [POWER, [NumConst [10], Unary [NEG, Binary [ADD, NumConst [1], NumConst [2]]]]]]]]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(avg);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, avg.toString());
        Assert.assertEquals(avg.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a function with a list argument
     * Check that the AST is ok (but without checking the generated code).<br>
     */
    @Test
    public void radomExp() throws Exception {
        Expr<Void> rand = expr2AST("e^randFloat()%exp(floor(randInt(1,10)))");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(rand);
        String t =
            "Binary [MOD, MathPowerFunct [POWER, [MathConst [E], FunctionExpr [MathRandomFloatFunct []]]], FunctionExpr [MathSingleFunct [EXP, [FunctionExpr [MathSingleFunct [ROUNDDOWN, [FunctionExpr [MathRandomIntFunct [[NumConst [1], NumConst [10]]]]]]]]]]]";
        String g = p.UnParse();
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(rand);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, rand.toString());
        Assert.assertEquals(rand.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a equality expression with math expressions.
     * Check that the AST is ok.<br>
     */
    @Test
    public void equalitytext() throws Exception {
        Expr<Void> neq = expr2AST("2==2");
        ExprlyUnParser<Void> p = new ExprlyUnParser<Void>(neq);
        String t = "Binary [EQ, NumConst [2], NumConst [2]]";
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(neq);
        Assert.assertTrue(0 == c.check());
        Assert.assertEquals(t, neq.toString());
        Assert.assertEquals(neq.toString(), expr2AST(p.UnParse()).toString());
    }

    /**
     * create a correct AST programmatically for a equality expression with some errors.
     * Check that the number of errors is the same as expected.<br>
     */
    @Test
    public void typeCheckEq() throws Exception {
        Expr<Void> eq = expr2AST(" \"Hello \" + 42 == !(0,0,0,0)");
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(eq);
        Assert.assertTrue(3 == c.check());
        System.out.println("typeCheckEq Test:");
        List<String> errors = c.getErrors();
        for ( String s : errors ) {
            System.out.println(s);
        }
        System.out.println("");
    }

    /**
     * create a correct AST programmatically for a single math function expression with some errors.
     * Check that the number of errors is the same as expected.<br>
     */
    @Test
    public void typeCheckMathSingleFunctCall() throws Exception {
        Expr<Void> f = expr2AST("phi^true");
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(f);
        Assert.assertTrue(1 == c.check());
        System.out.println("typeCheckMathSingleFunctionCall Test:");
        List<String> errors = c.getErrors();
        for ( String s : errors ) {
            System.out.println(s);
        }
        System.out.println("");
    }

    /**
     * create a correct AST programmatically for a math on list function expression with some errors.
     * Check that the number of errors is the same as expected.<br>
     */
    @Test
    public void typeCheckMathOnListFunct() throws Exception {
        Expr<Void> l = expr2AST("sum(1, 2, 3, 4, 5)+avg([])-median([1, randFloat()])");
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(l);
        Assert.assertTrue(7 == c.check());
        System.out.println("typeCheckMathOnListFunct Test:");
        List<String> errors = c.getErrors();
        for ( String s : errors ) {
            System.out.println(s);
        }
        System.out.println("");
    }

    /**
     * create a correct AST programmatically for a math on list function expression with some errors.
     * Check that the number of errors is the same as expected.<br>
     */
    @Test
    public void typeCheckMathOnListFunct1() throws Exception {
        Expr<Void> l = expr2AST("sum(1, 2, 3, 4, 5)+avg([])-median([x, randFloat()])");
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<Void>(l);
        Assert.assertTrue(9 == c.check());
        System.out.println("typeCheckMathOnListFunct1 Test:");
        List<String> errors = c.getErrors();
        for ( String s : errors ) {
            System.out.println(s);
        }
        System.out.println("");

    }

    /**
     * create a correct AST programmatically for a math on list function expression with some errors.
     * Check that exception is thrown <br>
     */
    @Test
    public void typeCheckMathRandFloatFunct() throws Exception {
        try {
            Expr<Void> l = expr2AST("randFloat(0, randInt(0, 2))");
        } catch ( UnsupportedOperationException e ) {
            System.out.println("Args.size() > 0 Error detected");
        }
    }

    @Test
    public void testFromXml() throws Exception {
        Phrase<Void> ast = this.h.generateAST("/expressionblock/eval_expr_1add2.xml");
        String t = "Binary [ADD, NumConst [1], NumConst [2]]";
        Assert.assertEquals(t, ast.toString());
        checkCode((Expr<Void>) ast);
        System.out.println("");

    }

    /**
     * function to print C++ and Python code generated with the ast
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
        ExprlyAST<Void> eval = new ExprlyAST<>();
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
