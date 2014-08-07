package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopMode;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class MotorStopActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[MotorStop [port=A, mode=FLOAT]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_MotorStop.xml"));
    }

    @Test
    public void getPort() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_MotorStop.xml");

        MotorStopAction mgp = (MotorStopAction) transformer.getTree().get(0);

        Assert.assertEquals(ActorPort.A, mgp.getPort());
    }

    @Test
    public void getMode() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_MotorStop.xml");

        MotorStopAction mgp = (MotorStopAction) transformer.getTree().get(0);

        Assert.assertEquals(MotorStopMode.FLOAT, mgp.getMode());
    }

}
