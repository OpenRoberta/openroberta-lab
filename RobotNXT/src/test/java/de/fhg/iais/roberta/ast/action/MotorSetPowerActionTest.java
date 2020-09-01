package de.fhg.iais.roberta.ast.action;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorSetPowerActionTest extends NxtAstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-98, y=182], MotorSetPowerAction [port=B, power=NumConst [30]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorSetPower.xml");
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_MotorSetPower.xml");
        MotorSetPowerAction<Void> mgp = (MotorSetPowerAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("B", mgp.getUserDefinedPort());
    }

    @Test
    public void getPower() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_MotorSetPower.xml");
        MotorSetPowerAction<Void> mgp = (MotorSetPowerAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("NumConst [30]", mgp.getPower().toString());
    }

    @Test
    public void powerMissing() throws Exception {
        String a = "BlockAST [project=[[Location [x=22, y=145], MotorSetPowerAction [port=B, power=EmptyExpr [defVal=NUMBER_INT]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorSetPowerMissing.xml");
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorSetPower.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorSetPower1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorSetPower2.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorSetPowerMissing.xml");
    }

}
