package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst.Const;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathConstTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=131, y=-615], MathConst [E]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_constant1.xml");
    }

    @Test
    public void getMathConst() throws Exception {
        MathConst<Void> mathConst = (MathConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_constant1.xml");
        Assert.assertEquals(Const.E, mathConst.getMathConst());
    }

    @Test
    public void getPresedance() throws Exception {
        MathConst<Void> mathConst = (MathConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_constant1.xml");
        Assert.assertEquals(999, mathConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        MathConst<Void> mathConst = (MathConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_constant1.xml");
        Assert.assertEquals(Assoc.NONE, mathConst.getAssoc());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_constant.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_constant1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_constant2.xml");
    }

    @Test
    public void reverseTransformatin3() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_constant3.xml");
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        MathConst.Const.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        MathConst.Const.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        MathConst.Const.get("asdf");
    }
}
