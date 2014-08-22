package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnDirection;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class TurnActionTest {

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorDiffTurnFor.xml"));
    }

    @Test
    public void getDirection() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/actions/action_MotorDiffTurnFor.xml");

        TurnAction ta = (TurnAction) transformer.getTree().get(0);

        Assert.assertEquals(TurnDirection.RIGHT, ta.getDirection());
    }

    @Test
    public void getParam() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/actions/action_MotorDiffTurnFor.xml");

        TurnAction ta = (TurnAction) transformer.getTree().get(0);

        Assert.assertEquals("MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]", ta.getParam().toString());
    }

    @Test
    public void motorDiffTurn() throws Exception {
        String a = "BlockAST [project=[[TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=null]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorDiffTurn.xml"));
    }
}
