package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class UnaryTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-46, y=111], Unary [NEG, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_single1.xml");
    }

    @Test
    public void getOp() throws Exception {
        Unary<Void> unary = (Unary<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_single1.xml");
        Assert.assertEquals(Unary.Op.NEG, unary.getOp());
    }

    @Test
    public void getExpr() throws Exception {
        Unary<Void> unary = (Unary<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_single1.xml");
        Assert.assertEquals("NumConst [10]", unary.getExpr().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        Unary<Void> unary = (Unary<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_single1.xml");
        Assert.assertEquals(10, unary.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Unary<Void> unary = (Unary<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/math/math_single1.xml");
        Assert.assertEquals(Assoc.LEFT, unary.getAssoc());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        Unary.Op.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        Unary.Op.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        Unary.Op.get("asdf");
    }
}
