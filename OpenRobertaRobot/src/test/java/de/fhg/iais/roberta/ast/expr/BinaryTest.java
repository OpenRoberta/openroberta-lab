package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BinaryTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=27, y=-575], Binary [ADD, NumConst [1], MathPowerFunct [POWER, [NumConst [5], NumConst [8]]]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_arithmetic.xml");
    }

    @Test
    public void getOp() throws Exception {
        Binary<Void> binary = (Binary<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_arithmetic.xml");
        Assert.assertEquals(Binary.Op.ADD, binary.getOp());
    }

    @Test
    public void getLeft() throws Exception {
        Binary<Void> binary = (Binary<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_arithmetic.xml");
        Assert.assertEquals("NumConst [1]", binary.getLeft().toString());
    }

    @Test
    public void getRight() throws Exception {
        Binary<Void> binary = (Binary<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_arithmetic.xml");
        Assert.assertEquals("MathPowerFunct [POWER, [NumConst [5], NumConst [8]]]", binary.getRight().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        Binary<Void> binary = (Binary<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_arithmetic.xml");
        Assert.assertEquals(100, binary.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Binary<Void> binary = (Binary<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_arithmetic.xml");
        Assert.assertEquals(Assoc.LEFT, binary.getAssoc());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        Binary.Op.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        Binary.Op.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        Binary.Op.get("asdf");
    }
}
