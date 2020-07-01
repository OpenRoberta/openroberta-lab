package de.fhg.iais.roberta.syntax.expr;

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

import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.exprly.generated.ExprlyLexer;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser.ExpressionContext;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.eval.ExprlyTypechecker;
import de.fhg.iais.roberta.syntax.lang.expr.eval.ExprlyUnParser;
import de.fhg.iais.roberta.syntax.lang.expr.eval.ExprlyVisitor;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.codegen.CalliopeCppVisitor;
import de.fhg.iais.roberta.visitor.codegen.MicrobitPythonVisitor;

public class ExprlyBasicAstTest extends AstTest {
    private static final Logger LOG = LoggerFactory.getLogger(ExprlyBasicAstTest.class);

    /**
     * The purpose of this set of test is to try out simple expressions for the exprBlock and to check that the AST is ok. Every expression of the grammar is
     * tested here.
     */

    @Test
    public void nullTest() throws Exception {
        Expr<Void> e = expr2AST("null");
        String t = "NullConst [null]";
        test(e, t, BlocklyType.VOID);
    }

    @Test
    public void numBinaryTest() throws Exception {
        Expr<Void> e = expr2AST("1.25+(2^3/(3%3)-42*0)");
        String t =
            "Binary [ADD, NumConst [1.25], Binary [MINUS, "
                + "Binary [DIVIDE, MathPowerFunct [POWER, [NumConst [2], NumConst [3]]], "
                + "Binary [MOD, NumConst [3], NumConst [3]]], Binary [MULTIPLY, NumConst [42], NumConst [0]]]]";
        test(e, t, BlocklyType.NUMBER);
    }

    @Test
    public void boolBinaryTest() throws Exception {
        Expr<Void> e = expr2AST("true&&(false||true)");
        String t = "Binary [AND, BoolConst [true], Binary [OR, BoolConst [false], BoolConst [true]]]";
        test(e, t, BlocklyType.BOOLEAN);
    }

    @Test
    public void boolBinaryTest1() throws Exception {
        Expr<Void> e = expr2AST("(123>=32)==true");
        String t = "Binary [EQ, Binary [GTE, NumConst [123], NumConst [32]], BoolConst [true]]";
        test(e, t, BlocklyType.BOOLEAN);
    }

    @Test
    public void boolBinaryTest2() throws Exception {
        Expr<Void> e = expr2AST("(463<0)!=true");
        String t = "Binary [NEQ, Binary [LT, NumConst [463], NumConst [0]], BoolConst [true]]";
        test(e, t, BlocklyType.BOOLEAN);
    }

    @Test
    public void numUnaryTest() throws Exception {
        Expr<Void> e = expr2AST("-12");
        String t = "Unary [NEG, NumConst [12]]";
        test(e, t, BlocklyType.NUMBER);
    }

    @Test
    public void boolUnaryTest() throws Exception {
        Expr<Void> e = expr2AST("!false");
        String t = "Unary [NOT, BoolConst [false]]";
        test(e, t, BlocklyType.BOOLEAN);
    }

    @Test
    public void strTest() throws Exception {
        Expr<Void> e = expr2AST("\"Hello World!\"");
        String t = "StringConst [Hello World !]";
        test(e, t, BlocklyType.STRING);
    }

    @Test
    public void mathListTest() throws Exception {
        Expr<Void> e = expr2AST("([1,1+2,-(1+2)])");
        String t = "NumConst [1], Binary [ADD, NumConst [1], NumConst [2]], Unary [NEG, Binary [ADD, NumConst [1], NumConst [2]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void booleanListTest() throws Exception {
        Expr<Void> e = expr2AST("([true,false,!true, !!false])");
        String t = "BoolConst [true], BoolConst [false], " + "Unary [NOT, BoolConst [true]], " + "Unary [NOT, Unary [NOT, BoolConst [false]]]";
        test(e, t, BlocklyType.ARRAY_BOOLEAN);
    }

    @Test
    public void colorListTest() throws Exception {
        Expr<Void> e = expr2AST("([getRGB(0,0,0), getRGB(255,255,255,255)])");
        String t = "" + //
            "RgbColor [NumConst [0], NumConst [0], NumConst [0], EmptyExpr [defVal=NUMBER_INT]], "
            + "RgbColor [NumConst [255], NumConst [255], NumConst [255], NumConst [255]]";
        test(e, t, BlocklyType.ARRAY_COLOUR);
    }

    @Test
    public void emptyListTest() throws Exception {
        Expr<Void> e = expr2AST("[]");
        String t = "";
        test(e, t, BlocklyType.ARRAY);
    }

    @Test
    public void mathConstTest() throws Exception {
        Expr<Void> e = expr2AST("phi+pi-e*sqrt2+sqrt_1_2-inf");
        String t =
            "Binary [MINUS, Binary [ADD, Binary [MINUS, "
                + "Binary [ADD, MathConst [GOLDEN_RATIO], MathConst [PI]], "
                + "Binary [MULTIPLY, MathConst [E], MathConst [SQRT2]]], MathConst [SQRT1_2]], MathConst [INFINITY]]";
        test(e, t, BlocklyType.NUMBER);
    }

    @Test
    public void trigTest() throws Exception {
        Expr<Void> e = expr2AST("[sin(x),cos(y),tan(z),asin(pi),acos(-pi),atan(pi/2)]");
        String t =
            "FunctionExpr [MathSingleFunct [SIN, [Var [x]]]], "
                + "FunctionExpr [MathSingleFunct [COS, [Var [y]]]], "
                + "FunctionExpr [MathSingleFunct [TAN, [Var [z]]]], "
                + "FunctionExpr [MathSingleFunct [ASIN, [MathConst [PI]]]], "
                + "FunctionExpr [MathSingleFunct [ACOS, [Unary [NEG, MathConst [PI]]]]], "
                + "FunctionExpr [MathSingleFunct [ATAN, [Binary [DIVIDE, MathConst [PI], NumConst [2]]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void mathFunctTest() throws Exception {
        Expr<Void> e = expr2AST("[exp(12), sqrt(2), abs(-4), log10(10), ln(e)]");
        String t =
            "FunctionExpr [MathSingleFunct [EXP, [NumConst [12]]]], "
                + "FunctionExpr [MathSingleFunct [ROOT, [NumConst [2]]]], "
                + "FunctionExpr [MathSingleFunct [ABS, [Unary [NEG, NumConst [4]]]]], "
                + "FunctionExpr [MathSingleFunct [LOG10, [NumConst [10]]]], "
                + "FunctionExpr [MathSingleFunct [LN, [MathConst [E]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void randomTest() throws Exception {
        Expr<Void> e = expr2AST("randInt(0,10)/randFloat()+randItem([0,1,2,3])");
        String t =
            "Binary [ADD, Binary [DIVIDE, FunctionExpr "
                + "[MathRandomIntFunct [[NumConst [0], NumConst [10]]]], "
                + "FunctionExpr [MathRandomFloatFunct []]], "
                + "FunctionExpr [MathOnListFunct [RANDOM, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]]]]]]";
        test(e, t, BlocklyType.NUMBER);
    }

    @Test
    public void roundTest() throws Exception {
        Expr<Void> e = expr2AST("[roundUp(1.5), roundDown(1.5), round(.5)]");
        String t =
            "FunctionExpr [MathSingleFunct [ROUNDUP, [NumConst [1.5]]]], "
                + "FunctionExpr [MathSingleFunct [ROUNDDOWN, [NumConst [1.5]]]], "
                + "FunctionExpr [MathSingleFunct [ROUND, [NumConst [.5]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void propTest() throws Exception {
        Expr<Void> e = expr2AST("[isEven(6), isOdd(9), isWhole(3), isEmpty([1,2]), isPositive(4), isNegative(-2), isDivisibleBy(1,1)]");
        String t =
            "FunctionExpr [MathNumPropFunct [EVEN, [NumConst [6]]]], FunctionExpr [MathNumPropFunct [ODD, [NumConst [9]]]], FunctionExpr [MathNumPropFunct [WHOLE, [NumConst [3]]]], FunctionExpr [LengthOfFunct [LIST_IS_EMPTY, [ListCreate [ARRAY, NumConst [1], NumConst [2]]]]], FunctionExpr [MathNumPropFunct [POSITIVE, [NumConst [4]]]], FunctionExpr [MathNumPropFunct [NEGATIVE, [Unary [NEG, NumConst [2]]]]], FunctionExpr [MathNumPropFunct [DIVISIBLE_BY, [NumConst [1], NumConst [1]]]]";
        test(e, t, BlocklyType.ARRAY_BOOLEAN);
    }

    @Test
    public void mathOnListTest() throws Exception {
        Expr<Void> e = expr2AST("[sum([1,2,3,4]),max([1,2,3,4]),min([1,2,3,4]),avg([1,2,3,4]),median([1,2,3,4]),sd([1,2,3,4])]");
        String t =
            "FunctionExpr [MathOnListFunct [SUM, [ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4]]]]], FunctionExpr [MathOnListFunct [MAX, [ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4]]]]], FunctionExpr [MathOnListFunct [MIN, [ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4]]]]], FunctionExpr [MathOnListFunct [AVERAGE, [ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4]]]]], FunctionExpr [MathOnListFunct [MEDIAN, [ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4]]]]], FunctionExpr [MathOnListFunct [STD_DEV, [ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4]]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void lengthTest() throws Exception {
        Expr<Void> e = expr2AST("lengthOf([1,2,3])");
        String t = "FunctionExpr [LengthOfFunct [LIST_LENGTH, [ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3]]]]]";
        test(e, t, BlocklyType.NUMBER);
    }

    @Test
    public void indexOfTest() throws Exception {
        Expr<Void> e = expr2AST("[indexOfFirst([1,2,3,4],0), indexOfLast([1,2,3,4],0)]");
        String t =
            "FunctionExpr [IndexOfFunct [FIRST, [ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4]], NumConst [0]]]], FunctionExpr [IndexOfFunct [LAST, [ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4]], NumConst [0]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void getIndexTest() throws Exception {
        Expr<Void> e =
            expr2AST(
                "[getIndex([0,1,2,3], 0),"
                    + "getIndexFromEnd([0,1,2,3], 0),"
                    + "getIndexFirst([0,1,2,3]),"
                    + "getIndexLast([0,1,2,3]),"
                    + "getAndRemoveIndex([0,1,2,3], 0),"
                    + "getAndRemoveIndexFromEnd([0,1,2,3], 0),"
                    + "getAndRemoveIndexFirst([0,1,2,3]),"
                    + "getAndRemoveIndexLast([0,1,2,3]),"
                    + "removeIndex([0,1,2,3], 0),"
                    + "removeIndexFromEnd([0,1,2,3], 0),"
                    + "removeIndexFirst([0,1,2,3]),"
                    + "removeIndexLast([0,1,2,3]) ]");
        String t =
            "FunctionExpr [ListGetIndex [GET, FROM_START, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]], NumConst [0]]]], "
                + "FunctionExpr [ListGetIndex [GET, FROM_END, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]], NumConst [0]]]], "
                + "FunctionExpr [ListGetIndex [GET, FIRST, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]]]]], "
                + "FunctionExpr [ListGetIndex [GET, LAST, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]]]]], "
                + "FunctionExpr [ListGetIndex [GET_REMOVE, FROM_START, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]], NumConst [0]]]], "
                + "FunctionExpr [ListGetIndex [GET_REMOVE, FROM_END, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]], NumConst [0]]]], "
                + "FunctionExpr [ListGetIndex [GET_REMOVE, FIRST, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]]]]], "
                + "FunctionExpr [ListGetIndex [GET_REMOVE, LAST, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]]]]], "
                + "FunctionExpr [ListGetIndex [REMOVE, FROM_START, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]], NumConst [0]]]], "
                + "FunctionExpr [ListGetIndex [REMOVE, FROM_END, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]], NumConst [0]]]], "
                + "FunctionExpr [ListGetIndex [REMOVE, FIRST, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]]]]], "
                + "FunctionExpr [ListGetIndex [REMOVE, LAST, [ListCreate [ARRAY, NumConst [0], NumConst [1], NumConst [2], NumConst [3]]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void repeatListTest() throws Exception {
        Expr<Void> e = expr2AST("repeatList(5,5)");
        String t = "FunctionExpr [ListRepeat [VOID, [NumConst [5], NumConst [5]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void subListTest() throws Exception {
        Expr<Void> e = expr2AST("subList([1,2,3], 0, 1)");
        String t =
            "FunctionExpr [GetSubFunct [GET_SUBLIST, [FROM_START, FROM_START], [ListCreate "
                + "[ARRAY, NumConst [1], NumConst [2], NumConst [3]], NumConst [0], NumConst [1]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void subListFromIndexToLastTest() throws Exception {
        Expr<Void> e = expr2AST("subListFromIndexToLast([1,2,3,4,5], 3)");
        String t =
            "FunctionExpr [GetSubFunct [GET_SUBLIST, [FROM_START, LAST], [ListCreate "
                + "[ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4], NumConst [5]], "
                + "NumConst [3]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void subListFromIndexToEndTest() throws Exception {
        Expr<Void> e = expr2AST("subListFromIndexToEnd([\" Hello \",\" New \",\" York \",\"!\"], 1,1)");
        String t =
            "FunctionExpr [GetSubFunct [GET_SUBLIST, [FROM_START, FROM_END], "
                + "[ListCreate [ARRAY, StringConst [Hello], StringConst [New], StringConst [York], "
                + "StringConst [!]], NumConst [1], NumConst [1]]]]";
        test(e, t, BlocklyType.ARRAY_STRING);
    }

    @Test
    public void subListFromFirstToIndexTest() throws Exception {
        Expr<Void> e = expr2AST("subListFromFirstToIndex([10,10,10,22,4], 2)");
        String t =
            "FunctionExpr [GetSubFunct [GET_SUBLIST, [FIRST, FROM_START], "
                + "[ListCreate [ARRAY, NumConst [10], NumConst [10], NumConst [10], "
                + "NumConst [22], NumConst [4]], NumConst [2]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void subListFromFirstToLastTest() throws Exception {
        //Note: this array can also be seen as 101010=42
        Expr<Void> e = expr2AST("subListFromFirstToLast([true, false, true, false, true, false])");
        String t =
            "FunctionExpr [GetSubFunct [GET_SUBLIST, [FIRST, LAST], "
                + "[ListCreate [ARRAY, BoolConst [true], BoolConst [false], BoolConst [true], "
                + "BoolConst [false], BoolConst [true], BoolConst [false]]]]]";
        test(e, t, BlocklyType.ARRAY_BOOLEAN);
    }

    @Test
    public void subListFromFirstToEndTest() throws Exception {
        // Note: the resulting array will be [6, 7] because 6*7 = 42
        Expr<Void> e = expr2AST("subListFromFirstToEnd([6, 7, 0],3)");
        String t =
            "FunctionExpr [GetSubFunct [GET_SUBLIST, [FIRST, FROM_END], " + "[ListCreate [ARRAY, NumConst [6], NumConst [7], NumConst [0]], NumConst [3]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void subListFromEndToIndexTest() throws Exception {
        Expr<Void> e = expr2AST("subListFromEndToIndex([2, 12, 32, 42],1,1)");
        String t =
            "FunctionExpr [GetSubFunct [GET_SUBLIST, [FROM_END, FROM_START], "
                + "[ListCreate [ARRAY, NumConst [2], NumConst [12], NumConst [32], NumConst [42]], NumConst [1], NumConst [1]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void subListFromEndToEndTest() throws Exception {
        Expr<Void> e = expr2AST("subListFromEndToEnd([0,0,0,0,0], 3, 1)");
        String t =
            "FunctionExpr [GetSubFunct [GET_SUBLIST, [FROM_END, FROM_END], "
                + "[ListCreate [ARRAY, NumConst [0], NumConst [0], NumConst [0], NumConst [0], NumConst [0]], "
                + "NumConst [3], NumConst [1]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void subListFromEndToLastTest() throws Exception {
        Expr<Void> e = expr2AST("subListFromEndToLast([1,2,3,4,5], 1)");
        String t =
            "FunctionExpr [GetSubFunct [GET_SUBLIST, [FROM_END, LAST], "
                + "[ListCreate [ARRAY, NumConst [1], NumConst [2], NumConst [3], NumConst [4], NumConst [5]],"
                + " NumConst [1]]]]";
        test(e, t, BlocklyType.ARRAY_NUMBER);
    }

    @Test
    public void createTextWithTest() throws Exception {
        Expr<Void> e = expr2AST("createTextWith(\"This \", \"is \", \"SPARTAAAAA\", \"!\")");
        String t = "FunctionExpr [TextJoinFunct [StringConst [This], StringConst [is], StringConst [SPARTAAAAA], StringConst [!]]]";
        test(e, t, BlocklyType.STRING);
    }

    @Test
    public void constrainTest() throws Exception {
        Expr<Void> e = expr2AST("constrain(1,2,3)");
        String t = "FunctionExpr [MathConstrainFunct [[NumConst [1], NumConst [2], NumConst [3]]]]";
        test(e, t, BlocklyType.NUMBER);
    }

    @Test
    public void ternaryTest() throws Exception {
        Expr<Void> e = expr2AST("true||false?1:2");
        String t =
            "StmtExpr [\n"
                + "if Binary [OR, BoolConst [true], BoolConst [false]]\n"
                + ",then\n"
                + "exprStmt NumConst [1]\n"
                + ",else\n"
                + "exprStmt NumConst [2]\n"
                + "]";
        test(e, t, BlocklyType.NUMBER);
    }

    @Test
    public void testFromXml() throws Exception {
        Phrase<Void> ast = UnitTestHelper.getProgramAst(testFactory, "/expressionblock/eval_expr_1add2.xml").get(0).get(1);
        String t = "Binary [ADD, NumConst [1], NumConst [2]]";
        Assert.assertEquals(t, ast.toString());
        checkCode((Expr<Void>) ast);
        System.out.println("");

    }

    /**
     * Function to do the testing
     */
    public void test(Expr<Void> e, String t, BlocklyType b) throws Exception {
        ExprlyTypechecker<Void> c = new ExprlyTypechecker<>(e, b);
        ExprlyUnParser<Void> p = new ExprlyUnParser<>(e);
        c.check();
        Assert.assertTrue(0 == c.getNumErrors());
        Assert.assertEquals(t, e.toString());
        Assert.assertEquals(e.toString(), expr2AST(p.fullUnParse()).toString());
        if ( e instanceof FunctionExpr<?> ) {
            if ( ((FunctionExpr<Void>) e).getFunction() instanceof ListRepeat ) {
                return;
            }
        }
        if ( e.isReadOnly() ) {
            checkCode(e);
        }
    }

    /**
     * function to print C++ and Python code generated with the ast
     */
    private void checkCode(Expr<Void> expr) {
        String helperFile = testFactory.getPluginProperties().getStringProperty("robot.helperMethods");
        UsedHardwareBean usedHardwareBean = new UsedHardwareBean.Builder().build();
        CodeGeneratorSetupBean codeGeneratorSetupBeanCpp = new CodeGeneratorSetupBean.Builder().setHelperMethodFile(helperFile).setFileExtension("cpp").build();
        CodeGeneratorSetupBean codeGeneratorSetupBeanPy = new CodeGeneratorSetupBean.Builder().setHelperMethodFile(helperFile).setFileExtension("py").build();
        ImmutableClassToInstanceMap<IProjectBean> beansCpp = ImmutableClassToInstanceMap.<IProjectBean>builder().put(UsedHardwareBean.class, usedHardwareBean).put(
            CodeGeneratorSetupBean.class,
            codeGeneratorSetupBeanCpp).build();
        ImmutableClassToInstanceMap<IProjectBean> beansPy = ImmutableClassToInstanceMap.<IProjectBean>builder().put(UsedHardwareBean.class, usedHardwareBean).put(
            CodeGeneratorSetupBean.class,
            codeGeneratorSetupBeanPy).build();
        List<Phrase<Void>> addInList = new ArrayList<>();
        addInList.add(expr);
        List<List<Phrase<Void>>> addInListInList = new ArrayList<>();
        addInListInList.add(addInList);
        CalliopeCppVisitor cppVisitor = new CalliopeCppVisitor(addInListInList, null, beansCpp);
        cppVisitor.visitExprStmt(ExprStmt.make(expr));
        LOG.info("generated C++ code: " + cppVisitor.getSb().toString());
        MicrobitPythonVisitor pythonVisitor = new MicrobitPythonVisitor(addInListInList, null, beansPy);
        pythonVisitor.visitExprStmt(ExprStmt.make(expr));
        LOG.info("generated Python code: " + pythonVisitor.getSb().toString());
    }

    /**
     * Function to create ast
     */
    private Expr<Void> expr2AST(String expr) throws Exception {
        ExprlyParser parser = mkParser(expr);
        ExprlyVisitor<Void> eval = new ExprlyVisitor<>();
        ExpressionContext expression = parser.expression();
        Expr<Void> block = eval.visitExpression(expression);
        block.setReadOnly();
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
