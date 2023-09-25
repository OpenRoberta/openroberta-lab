package de.fhg.iais.roberta.typecheck;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.AstTest;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.validate.TypecheckCommonLanguageVisitor;

public class TypecheckEvalExprTest extends AstTest {
    public static final boolean VERBOSE = false;

    public static final boolean FAIL = false;
    public static final BlocklyType BOOL = BlocklyType.BOOLEAN;
    public static final BlocklyType NUMBER = BlocklyType.NUMBER;
    public static final BlocklyType STRING = BlocklyType.STRING;
    public static final BlocklyType COLOR = BlocklyType.COLOR;
    public static final BlocklyType ARRAY_NUMBER = BlocklyType.ARRAY_NUMBER;
    private ClassToInstanceMap<IProjectBean.IBuilder> beans;
    private String evalExprTemplate;

    private List<String> errorMessages = new ArrayList<>();

    @Before
    public void setup() throws Exception {
        UsedHardwareBean.Builder usedHardware = new UsedHardwareBean.Builder();
        usedHardware.addDeclaredVariable("n", BlocklyType.NUMBER);
        usedHardware.addDeclaredVariable("b", BlocklyType.BOOLEAN);
        usedHardware.addDeclaredVariable("s", BlocklyType.STRING);
        usedHardware.addDeclaredVariable("c", BlocklyType.COLOR);
        usedHardware.addDeclaredVariable("nl", BlocklyType.ARRAY_NUMBER);
        usedHardware.addDeclaredVariable("bl", BlocklyType.ARRAY_BOOLEAN);
        usedHardware.addDeclaredVariable("sl", BlocklyType.ARRAY_STRING);
        usedHardware.addDeclaredVariable("cl", BlocklyType.ARRAY_COLOUR);
        ImmutableClassToInstanceMap.Builder beansBuilder = new ImmutableClassToInstanceMap.Builder();
        beansBuilder.put(UsedHardwareBean.Builder.class, usedHardware);
        this.beans = beansBuilder.build();
        this.evalExprTemplate = IOUtils.toString(JaxbHelper.class.getResourceAsStream("/ast/expressions/evalExprTemplate.xml"), "UTF-8");
    }

    @Test
    /**
     * tests for typechecking NEPO programs presented as Strings. The variables as defined above in the {@link setup()} method can be used.
     */
    public void testEvalExpr() throws Exception {
        errorMessages = new ArrayList<>();

        // Constants
        testEvalExpr(BOOL, "b", true);
        testEvalExpr(STRING, "s", true);
        testEvalExpr(NUMBER, "n", true);
        testEvalExpr(BOOL, "n", false);
        testEvalExpr(NUMBER, "b", false);

        // TODO: Null constant error, must be solved! testEvalExpr(BlocklyType.NULL, "null", true);
        // MathConst:
        testEvalExpr(NUMBER, "pi", true);
        testEvalExpr(NUMBER, "Pi", false);
        testEvalExpr(NUMBER, "sqrt_1_2", true);
        testEvalExpr(STRING, "sqrt_1_2", false);

        // LiteralExp:
        // TODO: ConstStr error, must be solved! testEvalExpr(STRING, "Hello world!", true);
        testEvalExpr(COLOR, "#green", true);
        testEvalExpr(COLOR, "#none", true);
        testEvalExpr(COLOR, "#BLACK", false);
        testEvalExpr(COLOR, "#rgb(F7D117)", true);

        testEvalExpr(NUMBER, "1", true);
        testEvalExpr(NUMBER, "true", false);
        testEvalExpr(NUMBER, "1.4", true);
        testEvalExpr(BOOL, "true", true);
        testEvalExpr(BOOL, "TRUE", false);

        // TODO: problem when you define a list only with constans ! testEvalExpr(BlocklyType.ARRAY_NUMBER, "[n,n,n]", true);
        testEvalExpr(ARRAY_NUMBER, "[1,2,3,4,5]", true);
        testEvalExpr(ARRAY_NUMBER, "[1,2,true,4,5]", false);
        testEvalExpr(BlocklyType.ARRAY_BOOLEAN, "[true,false,true]", true);

        // Parentheses
        testEvalExpr(NUMBER, "( n + 9 )", true);
        testEvalExpr(NUMBER, "((((3))))", true);
        testEvalExpr(BOOL, "( 1 + 2", false);

        // UnaryN
        testEvalExpr(NUMBER, "+1", true);
        testEvalExpr(NUMBER, "-1", true);
        testEvalExpr(NUMBER, "+-1", true);
        testEvalExpr(NUMBER, "-+1", true);

        // UnaryB
        testEvalExpr(BOOL, "!true", true);
        testEvalExpr(BOOL, "!!b", true);
        testEvalExpr(NUMBER, "!false", false);

        // BinaryN
        testEvalExpr(NUMBER, "2^2", true);
        testEvalExpr(NUMBER, "n^n", true);

        testEvalExpr(NUMBER, "5%2", true);
        testEvalExpr(NUMBER, "9%%3", false);

        testEvalExpr(NUMBER, "10*10", true);
        testEvalExpr(NUMBER, "100/10", true);
        testEvalExpr(NUMBER, "100/true", false);

        testEvalExpr(NUMBER, "1+2", true);
        testEvalExpr(BOOL, "1+2", false);
        testEvalExpr(NUMBER, "pi+e", true);
        testEvalExpr(NUMBER, "pi+4.5", true);
        testEvalExpr(NUMBER, "randFloat()+cos(n)", true);

        // BinaryB
        // TODO: severe error, must be solved! testEvalExpr(BlocklyType.BOOLEAN, "b && true", true);

        testEvalExpr(BOOL, "b || true", true);
        testEvalExpr(NUMBER, "false || true", false);
        testEvalExpr(BOOL, "b || pi", false);

        testEvalExpr(BOOL, "7 == 7", true);
        testEvalExpr(BOOL, " s == 2 ", false);
        testEvalExpr(BOOL, " s == s ", true);
        testEvalExpr(BOOL, " [1,2,3] != [1,2,3] ", true);
        testEvalExpr(BOOL, " bl != sl ", false);


        testEvalExpr(BOOL, "10 > 0", true);
        testEvalExpr(BOOL, "pi > 20", true);
        testEvalExpr(BOOL, "#black > #black", false);
        testEvalExpr(BOOL, "n > 4", true);
        testEvalExpr(BOOL, "n >= n", true);
        // TODO:  LET operator need to be solved! testEvalExpr(BOOL, "1 < 20", true);
        // TODO:  GEQ operator need to be solved! testEvalExpr(BOOL, "2 <= 3", true);

        // IfElseOp or Ternary Operations
        testEvalExpr(BOOL, "(n > 4) ? (n == 2) : (n+1 == n++3)", true);
        testEvalExpr(NUMBER, "true ? 1 : 2", true);
        testEvalExpr(NUMBER, "4 ? 1 : 2", false);
        testEvalExpr(NUMBER, "true ? 1 : true", false);

        // FNAME or Functions
        // math Trigonometric Functions
        testEvalExpr(NUMBER, "sin(n)", true);
        testEvalExpr(NUMBER, "cos(b)", false);
        testEvalExpr(NUMBER, "(sin(45)+cos(45))*tan(60)", true);
        testEvalExpr(BOOL, "Acos(45)", false);

        // math Single Functions
        // TODO: pow10(n) does not work, need to be solved! testEvalExpr(NUMBER, "pow10(2)", true);
        testEvalExpr(NUMBER, "exp(n)", true);
        testEvalExpr(NUMBER, "log10(n)*(square(10))", true);
        testEvalExpr(NUMBER, "log10(nl)", false);

        // random Functions
        testEvalExpr(NUMBER, "randInt(1,100)", true);
        testEvalExpr(NUMBER, "randFloat()", true);
        testEvalExpr(NUMBER, "randFloat", false);

        // round Functions
        testEvalExpr(NUMBER, "roundUp(3.782)+roundDown(3.1782)", true);
        testEvalExpr(NUMBER, "round(phi)", true);
        testEvalExpr(NUMBER, "round(nl)", false);

        // math NumProp Functions
        testEvalExpr(BOOL, "isPositive(----6)", true);
        testEvalExpr(BOOL, "isDivisibleBy(6,2)", true);
        testEvalExpr(BOOL, "isWhole(6.21234)", true);
        testEvalExpr(BOOL, "isPrime(6)", true);
        testEvalExpr(BOOL, "isEven(b)", false);
        testEvalExpr(STRING, "isOdd(3)", false);
        testEvalExpr(BOOL, "isDivisibleBy(6,true)", false);

        // is Empty Function
        // TODO: error when evaluate a list which is not defined before: testEvalExpr(BOOL, "isEmpty([1,2,3])", true); SOLVED ?
        testEvalExpr(BOOL, "isEmpty(nl)", true);
        testEvalExpr(BOOL, "isEmpty(bl)", true);
        testEvalExpr(BOOL, "isEmpty([true,false,true])", true);
        testEvalExpr(BOOL, "isEmpty([1,2,0])", true);
        testEvalExpr(BOOL, "isEmpty([#black,#green,#yellow,#rgb(000000)])", true);
        testEvalExpr(BOOL, "isEmpty([true,1,#black])", false);
        testEvalExpr(BOOL, "isEmpty([2,n,n])", true);

        // math OnList Functions
        // TODO: repair testEvalExpr(BlocklyType.NUMBER, "randItem(nl)", true); SOLVED ?
        // TODO: error when evaluate a list which is not defined before: testEvalExpr(NUMBER, "max([1,2,3])", true); SOLVED?
        testEvalExpr(NUMBER, "min(nl)", true);
        testEvalExpr(NUMBER, "max(nl)*avg(nl)", true);
        testEvalExpr(NUMBER, "sum(nl)", true);
        testEvalExpr(NUMBER, "median(cl)", false);
        testEvalExpr(NUMBER, "min([4,0,-2])", true);
        testEvalExpr(NUMBER, "median([80,10,10])", true);
        testEvalExpr(NUMBER, "max([true,false,false])", false);
        testEvalExpr(NUMBER, "randItem([1,2,3])", true);
        testEvalExpr(NUMBER, "randItem(nl)", true);
        testEvalExpr(BOOL, "randItem([true,false,true])", true);
        testEvalExpr(STRING, "randItem(sl)", true);

        // Lenght and index Functions
        // TODO: same problem with the math on list functions, does not work with non-defined list: testEvalExpr(NUMBER, "lengthOf([0,2,2],2)", true); SOLVED?
        // TODO: indexOfLast and indexOfFirst work only with list of numbers but not the other ones: testEvalExpr(NUMBER, "indexOfFirst(bl,true)", true); SOLVED?
        testEvalExpr(NUMBER, "lengthOf(cl)", true);
        testEvalExpr(NUMBER, "lengthOf([0,2,2])", true);
        testEvalExpr(NUMBER, "lengthOf(bl)", true);
        testEvalExpr(NUMBER, "lengthOf([#black,#green,#yellow,#rgb(000000)])", true);
        testEvalExpr(NUMBER, "lengthOf([0,true,#yellow])", false);

        testEvalExpr(NUMBER, "indexOfFirst([0,0,2],2)", true);
        testEvalExpr(NUMBER, "indexOfLast(nl,2)", true);
        testEvalExpr(NUMBER, "indexOfFirst(bl,true)", true);
        testEvalExpr(NUMBER, "indexOfLast([#black,#yellow],#green)", true);


        // ListGetIndex Functions
        // TODO: Solve problems with CAPTURE_TYPE: testEvalExpr(BOOL, "getIndexFirst([true,false,true])", true); SOLVED ?
        testEvalExpr(NUMBER, "getIndexFirst([1,2,3])", true);
        testEvalExpr(STRING, "getIndexFirst([1,2,4])", false);
        testEvalExpr(BOOL, "getIndexLast([false,true,false])", true);
        testEvalExpr(COLOR, "getIndex([#black,#green,#yellow],0)", true);
        testEvalExpr(COLOR, "getIndex([#black,#green,#yellow],#yellow)", false);
        testEvalExpr(COLOR, "getIndexFromEnd([#black,#green,#yellow],0)", true);

        testEvalExpr(NUMBER, "getAndRemoveIndex([1,2,3],2)", true);
        testEvalExpr(COLOR, "getAndRemoveIndexFromEnd([#black,#green,#yellow],0)", true);
        testEvalExpr(BOOL, "getAndRemoveIndexFirst([true,false,false,true])", true);
        testEvalExpr(NUMBER, "getAndRemoveIndexFirst([true,false,false,true])", false);

        // GetSub Functions
        testEvalExpr(BlocklyType.ARRAY_NUMBER, "subList([1,2,3,4,5],0,3)", true);
        testEvalExpr(BlocklyType.ARRAY_BOOLEAN, "subList([true,false,false,true,true],1,2)", true);
        testEvalExpr(BlocklyType.ARRAY_NUMBER, "subListFromIndexToLast([1,2,3,4,5],2)", true);
        testEvalExpr(BlocklyType.ARRAY_NUMBER, "subListFromIndexToEnd([1,2,3,4,5],2,5)", true);
        testEvalExpr(BlocklyType.ARRAY_BOOLEAN, "subListFromFirstToIndex([true,false,false,false,true],2)", true);
        testEvalExpr(BlocklyType.ARRAY_COLOUR, "subListFromFirstToLast([#black,#white,#yellow,#green,#red])", true);
        testEvalExpr(BlocklyType.ARRAY_COLOUR, "subListFromFirstToEnd([#black,#white,#yellow,#green,#red],2)", true);
        testEvalExpr(BlocklyType.ARRAY_NUMBER, "subListFromEndToIndex([1,2,3,4,5,6],5,2)", true);
        testEvalExpr(BlocklyType.ARRAY_STRING, "subListFromEndToEnd([1,2,3,4,5,6],5,4)", false);

        // TextJoin Functions
        testEvalExpr(STRING, "createTextWith(s,12,true,#black)", true);
        testEvalExpr(STRING, "createTextWith(True,#green)", false);
        testEvalExpr(BOOL, "createTextWith(s,nl,false)", false);

        // MathConstrain Functions
        testEvalExpr(NUMBER, "constrain(102,1,100)", true);
        testEvalExpr(STRING, "constrain(102,1,100)", false);

        showErrorsAndFailWithErrors();
    }

    @Test
    @Ignore
    /**
     * tests for typechecking one NEPO programs presented as String. The variables as defined above in the {@link setup()} method can be used.
     */
    public void testEvalExprSingle() throws Exception {
        errorMessages = new ArrayList<>();
        testEvalExpr(NUMBER, "indexOfFirst(bl,true)", true);
        showErrorsAndFailWithErrors();
    }

    /**
     * run a test for the evalExpr block
     *
     * @param type that the evalExpr block should return, when it is executed
     * @param eval the string to be evaluated
     * @param shouldSucceed true, if the typecheck is expected to succeed; false, if the typecheck should fail
     */
    private void testEvalExpr(BlocklyType type, String eval, boolean shouldSucceed) {
        String typeAsString = type.getBlocklyName();
        String testCaseMessage = "typechecking \"" + eval + "\" as " + typeAsString + " and expecting " + (shouldSucceed ? "success" : "failure");
        TypecheckCommonLanguageVisitor typechecker = null;
        try {
            if ( VERBOSE ) {
                System.out.println("***** " + testCaseMessage);
            }
            String evalExpr = evalExprTemplate.replaceAll("--TYPE--", typeAsString).replaceAll("--EVAL--", eval);
            BlockSet evalExprBlockSet = JaxbHelper.xml2BlockSet(evalExpr);
            Jaxb2ProgramAst transformer = new Jaxb2ProgramAst(testFactory);
            List<List<Phrase>> tree = transformer.blocks2ast(evalExprBlockSet).getTree();
            Phrase evalBlock = ((AssignStmt) tree.get(0).get(1)).expr;
            typechecker = TypecheckCommonLanguageVisitor.makeVisitorAndTypecheck(evalBlock, beans);
            if ( (typechecker.getErrorCount() == 0) != shouldSucceed ) {
                if ( !VERBOSE ) {
                    System.out.println("----- ERROR when " + testCaseMessage);
                }
                errorMessages.add(testCaseMessage + " ... failed");
            }
            if ( typechecker.getErrorCount() > 0 && (shouldSucceed || (!shouldSucceed && VERBOSE)) ) {
                System.out.println("      error messages:");
                typechecker.getInfos().stream().map(i -> "        " + i.getMessage()).forEach(System.out::println);
            }
            if ( (typechecker.getErrorCount() == 0) != shouldSucceed ) {
                System.out.println("      ERROR");
            } else {
                if ( VERBOSE ) {
                    System.out.println("      ok");
                }
            }
        } catch ( Exception e ) {
            if ( VERBOSE ) {
                System.out.println("      EXCEPTION");
                e.printStackTrace();
            } else {
                System.out.println("----- EXCEPTION when " + testCaseMessage);
                e.printStackTrace();
            }
            System.out.println("      ERROR");
            errorMessages.add(testCaseMessage + " ... failed with exception");
        }

    }

    private void showErrorsAndFailWithErrors() {
        if ( errorMessages.size() > 0 ) {
            System.out.println("");
            System.out.println("ERRORS:");
            for ( String msg : errorMessages ) {
                System.out.println("  " + msg);
            }
            Assert.fail();
        } else {
            System.out.println("typechecking terminated without errors");
        }
    }
}
