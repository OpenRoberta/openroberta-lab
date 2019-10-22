package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BoolConstTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=171], BoolConst [true]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/logic/logic_boolConst.xml");
    }

    @Test
    public void isValue() throws Exception {
        BoolConst<Void> boolConst = (BoolConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/logic/logic_boolConst.xml");
        Assert.assertEquals(true, boolConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        BoolConst<Void> boolConst = (BoolConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/logic/logic_boolConst.xml");
        Assert.assertEquals(999, boolConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        BoolConst<Void> boolConst = (BoolConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/logic/logic_boolConst.xml");
        Assert.assertEquals(Assoc.NONE, boolConst.getAssoc());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/logic/logic_boolConst.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/logic/logic_boolConst1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/logic/logic_boolConst2.xml");
    }
}
