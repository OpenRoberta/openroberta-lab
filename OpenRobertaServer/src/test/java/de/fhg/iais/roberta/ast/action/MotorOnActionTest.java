package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class MotorOnActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[MotorOnAction [B, MotionParam [speed=NumConst [30], duration=null]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_MotorOn.xml"));
    }

    @Test
    public void getParam() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_MotorOnFor.xml");

        MotorOnAction mo = (MotorOnAction) transformer.getTree().get(0);

        Assert.assertEquals("MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=NumConst [1]]]", mo.getParam().toString());
    }

    @Test
    public void getPort() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_MotorOnFor.xml");

        MotorOnAction mo = (MotorOnAction) transformer.getTree().get(0);

        Assert.assertEquals(ActorPort.B, mo.getPort());
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "BlockAST [project=[[MotorOnAction [B, MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=NumConst [1]]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_MotorOnFor.xml"));
    }
}
