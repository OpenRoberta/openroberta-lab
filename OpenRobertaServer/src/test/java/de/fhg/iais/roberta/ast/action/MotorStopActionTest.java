package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopMode;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class MotorStopActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-76, y=95], MotorStop [port=A, mode=FLOAT]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorStop.xml"));
    }

    @Test
    public void getPort() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorStop.xml");
        MotorStopAction<Void> mgp = (MotorStopAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(ActorPort.A, mgp.getPort());
    }

    @Test
    public void getMode() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorStop.xml");
        MotorStopAction<Void> mgp = (MotorStopAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(MotorStopMode.FLOAT, mgp.getMode());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorStop.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorStop1.xml");
    }
}
