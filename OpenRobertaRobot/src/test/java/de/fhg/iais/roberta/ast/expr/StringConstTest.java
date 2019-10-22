package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class StringConstTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-616, y=42], StringConst [text2]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/text/text_string_const.xml");
    }

    @Test
    public void getValue() throws Exception {
        StringConst<Void> stringConst = (StringConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/text/text_string_const.xml");
        Assert.assertEquals("text2", stringConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        StringConst<Void> stringConst = (StringConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/text/text_string_const.xml");
        Assert.assertEquals(999, stringConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        StringConst<Void> stringConst = (StringConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/text/text_string_const.xml");
        Assert.assertEquals(Assoc.NONE, stringConst.getAssoc());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/text/text_string_const.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/text/text_string_const1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/text/text_string_const2.xml");
    }

}
