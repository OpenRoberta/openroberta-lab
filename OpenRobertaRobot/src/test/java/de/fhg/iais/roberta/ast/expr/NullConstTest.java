package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class NullConstTest extends AstTest {

    @Test
    public void make() throws Exception {
        NullConst<Void> nullConst = NullConst.make(BlocklyBlockProperties.make("1", "1"), null);
        String a = "NullConst [null]";
        Assert.assertEquals(a, nullConst.toString());
    }

    @Test
    public void getValue() throws Exception {
        NullConst<Void> nullConst = NullConst.make(BlocklyBlockProperties.make("1", "1"), null);
        Assert.assertEquals(null, nullConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        NullConst<Void> nullConst = NullConst.make(BlocklyBlockProperties.make("1", "1"), null);
        Assert.assertEquals(999, nullConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        NullConst<Void> nullConst = NullConst.make(BlocklyBlockProperties.make("1", "1"), null);
        Assert.assertEquals(Assoc.NONE, nullConst.getAssoc());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/logic/logic_null.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/logic/logic_null1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/logic/logic_null2.xml");
    }

}
