package de.fhg.iais.roberta.ast;

import de.fhg.iais.roberta.util.test.edison.HelperEdisonForXmlTest;
import org.junit.Assert;
import org.junit.Test;

public class MathTest {

    HelperEdisonForXmlTest h = new HelperEdisonForXmlTest();

    private String insertIntoResult(String s) {
        return "BlockAST [project=" + s + "]";
    }

    //Math

    @Test
    public void TestInteger() throws Exception {
        String expected = insertIntoResult("[[Location [x=480, y=218], NumConst [0]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/math/integer.xml"));
    }

    @Test
    public void TestArithmetic() throws Exception { //sqrt(), log10(), abs(), 10^(), etc..
        String expected = insertIntoResult("[[Location [x=360, y=166], Binary [ADD, EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/math/arithmetic.xml"));
    }

    @Test
    public void TestSingle() throws Exception {
        String expected = insertIntoResult("[[Location [x=404, y=132], MathSingleFunct [ROOT, [EmptyExpr [defVal=NUMBER_INT]]]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/math/single.xml"));
    }

    @Test
    public void TestNumProp() throws Exception {
        String expected = insertIntoResult("[[Location [x=485, y=182], MathNumPropFunct [PRIME, [EmptyExpr [defVal=NUMBER_INT]]]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/math/numprop.xml"));
    }

    @Test
    public void TestChange() throws Exception {
        String expected = insertIntoResult("[[Location [x=349, y=133], \n" + "exprStmt Binary [MATH_CHANGE, EmptyExpr [defVal=STRING], NumConst [1]]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/math/change.xml"));
    }

    @Test
    public void TestRound() throws Exception {
        String expected = insertIntoResult("[[Location [x=428, y=213], MathSingleFunct [ROUNDDOWN, [EmptyExpr [defVal=NUMBER_INT]]]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/math/round.xml"));
    }

    @Test
    public void TestOnList() throws Exception {
        String expected = insertIntoResult("[[Location [x=426, y=224], MathOnListFunct [SUM, [EmptyExpr [defVal=ARRAY]]]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/math/on_list.xml"));
    }

    @Test
    public void TestModulo() throws Exception {
        String expected = insertIntoResult("[[Location [x=400, y=237], Binary [MOD, EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/math/modulo.xml"));
    }

}
