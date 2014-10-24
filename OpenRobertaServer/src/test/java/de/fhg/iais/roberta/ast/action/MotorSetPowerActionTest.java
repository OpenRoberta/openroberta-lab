package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class MotorSetPowerActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-98, y=182], MotorSetPowerAction [port=B, power=NumConst [30]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorSetPower.xml"));
    }

    @Test
    public void getPort() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorSetPower.xml");
        MotorSetPowerAction<Void> mgp = (MotorSetPowerAction<Void>) transformer.getTree().get(1);
        Assert.assertEquals(ActorPort.B, mgp.getPort());
    }

    @Test
    public void getPower() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorSetPower.xml");
        MotorSetPowerAction<Void> mgp = (MotorSetPowerAction<Void>) transformer.getTree().get(1);
        Assert.assertEquals("NumConst [30]", mgp.getPower().toString());
    }

    @Test
    public void powerMissing() throws Exception {
        String a = "BlockAST [project=[[Location [x=22, y=145], MotorSetPowerAction [port=B, power=EmptyExpr [defVal=class java.lang.Integer]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorSetPowerMissing.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorSetPower.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorSetPower1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorSetPower2.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorSetPowerMissing.xml");
    }

}
