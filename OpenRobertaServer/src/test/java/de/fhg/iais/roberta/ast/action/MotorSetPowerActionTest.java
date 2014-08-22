package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class MotorSetPowerActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[MotorSetPowerAction [port=B, power=NumConst [30]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorSetPower.xml"));
    }

    @Test
    public void getPort() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/actions/action_MotorSetPower.xml");

        MotorSetPowerAction mgp = (MotorSetPowerAction) transformer.getTree().get(0);

        Assert.assertEquals(ActorPort.B, mgp.getPort());
    }

    @Test
    public void getPower() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/actions/action_MotorSetPower.xml");

        MotorSetPowerAction mgp = (MotorSetPowerAction) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [30]", mgp.getPower().toString());
    }

}
