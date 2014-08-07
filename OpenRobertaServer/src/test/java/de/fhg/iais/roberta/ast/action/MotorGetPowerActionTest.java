package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class MotorGetPowerActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[MotorGetPower [port=B]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_MotorGetPower.xml"));
    }

    @Test
    public void getPort() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_MotorGetPower.xml");

        MotorGetPowerAction mgp = (MotorGetPowerAction) transformer.getTree().get(0);

        Assert.assertEquals(ActorPort.B, mgp.getPort());
    }

}
