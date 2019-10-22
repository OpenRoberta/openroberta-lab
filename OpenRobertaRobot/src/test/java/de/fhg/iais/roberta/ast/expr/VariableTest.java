package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class VariableTest extends AstTest {

    @Test
    public void variableSet() throws Exception {
        String a = "BlockAST [project=[[Location [x=-23, y=-797], Var [item]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/variables/variable_set1.xml");
    }

    @Test
    public void getValue() throws Exception {
        Var<Void> var = (Var<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/variables/variable_set1.xml");
        Assert.assertEquals("item", var.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        Var<Void> var = (Var<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/variables/variable_set1.xml");
        Assert.assertEquals(999, var.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Var<Void> var = (Var<Void>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/variables/variable_set1.xml");
        Assert.assertEquals(Assoc.NONE, var.getAssoc());
    }

    @Ignore
    public void variableSet4() throws Exception {
        String a = "BlockAST [project=[[Location [x=-23, y=-797], Var [item]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/variables/variable_set4.xml");
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/variables/variable_set.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/variables/variable_set1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/variables/variable_set2.xml");
    }

    @Test
    public void reverseTransformatin3() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/variables/variable_set3.xml");
    }

}
