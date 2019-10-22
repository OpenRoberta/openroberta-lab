package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ColorConstTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=116, y=139], ColorConst [#0057A6]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/colour/colour_const1.xml");
    }

    @Test
    public void isValue() throws Exception {
        ColorConst<Void> colorConst = (ColorConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/colour/colour_const1.xml");
        Assert.assertEquals("#0057A6", colorConst.getHexValueAsString());
    }

    @Test
    public void getPresedance() throws Exception {
        ColorConst<Void> colorConst = (ColorConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/colour/colour_const1.xml");
        Assert.assertEquals(999, colorConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        ColorConst<Void> colorConst = (ColorConst<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/colour/colour_const1.xml");
        Assert.assertEquals(Assoc.NONE, colorConst.getAssoc());
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/colour/colour_const1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/colour/colour_const2.xml");
    }

    @Test
    public void reverseTransformatin3() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/colour/colour_const3.xml");
    }

}
