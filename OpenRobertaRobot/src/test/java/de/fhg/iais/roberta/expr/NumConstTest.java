package de.fhg.iais.roberta.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.AstTest;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class NumConstTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-15, y=-845], NumConst [0]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_num_constant.xml");
    }

    @Test
    public void getValue() throws Exception {
        NumConst<Void> numConst = (NumConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_num_constant.xml");
        Assert.assertEquals("0", numConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        NumConst<Void> numConst = (NumConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_num_constant.xml");
        Assert.assertEquals(999, numConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        NumConst<Void> numConst = (NumConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_num_constant.xml");
        Assert.assertEquals(Assoc.NONE, numConst.getAssoc());
    }
}
