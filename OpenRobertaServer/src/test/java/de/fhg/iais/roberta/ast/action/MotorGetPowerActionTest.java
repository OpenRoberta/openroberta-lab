package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;

public class MotorGetPowerActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[MotorGetPower [port=B]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorGetPower.xml"));
    }

    @Test
    public void getPort() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorGetPower.xml");
        MotorGetPowerAction<Void> mgp = (MotorGetPowerAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals(ActorPort.B, mgp.getPort());
    }
}