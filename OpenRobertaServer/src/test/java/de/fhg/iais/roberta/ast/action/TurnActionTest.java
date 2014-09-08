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
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorDiffTurnFor.xml");
        TurnAction<Void> ta = (TurnAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals(TurnDirection.RIGHT, ta.getDirection());
    }

    @Test
    public void getParam() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorDiffTurnFor.xml");
        TurnAction<Void> ta = (TurnAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]", ta.getParam().toString());
    }

    @Test
    public void motorDiffTurn() throws Exception {
        String a = "BlockAST [project=[[TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=null]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorDiffTurn.xml"));
    }

    @Test
    public void motorDiffTurnMissing() throws Exception {
        String a = "BlockAST [project=[[TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=class java.lang.Integer], duration=null]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorDiffTurnMissing.xml"));
    }

    @Test
    public void motorDiffTurnForMissing() throws Exception {
        String a =
            "BlockAST [project=[[TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=class java.lang.Integer], duration=MotorDuration [type=DISTANCE, value=EmptyExpr [defVal=class java.lang.Integer]]]], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=class java.lang.Integer], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=EmptyExpr [defVal=class java.lang.Integer]]]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorDiffTurnForMissing.xml"));
    }
}
