package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class MotorGetPowerActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-78, y=63], MotorGetPower [port=B]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorGetPower.xml"));
    }

    @Test
    public void getPort() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorGetPower.xml");
        MotorGetPowerAction<Void> mgp = (MotorGetPowerAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(ActorPort.B, mgp.getPort());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorGetPower.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorGetPower1.xml");
    }

}