package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.action.ev3.TurnDirection;
import de.fhg.iais.roberta.syntax.action.ev3.TurnAction;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class TurnActionTest {

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=243], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorDiffTurnFor.xml"));
    }

    @Test
    public void getDirection() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorDiffTurnFor.xml");
        TurnAction<Void> ta = (TurnAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(TurnDirection.RIGHT, ta.getDirection());
    }

    @Test
    public void getParam() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_MotorDiffTurnFor.xml");
        TurnAction<Void> ta = (TurnAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("MotionParam [speed=NumConst [50], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]", ta.getParam().toString());
    }

    @Test
    public void motorDiffTurn() throws Exception {
        String a = "BlockAST [project=[[Location [x=29, y=89], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=null]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorDiffTurn.xml"));
    }

    @Test
    public void motorDiffTurnMissing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=35, y=84], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=class java.lang.Integer], duration=null]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorDiffTurnMissing.xml"));
    }

    @Test
    public void motorDiffTurnForMissing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=27, y=3], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=class java.lang.Integer], duration=MotorDuration [type=DEGREE, value=EmptyExpr [defVal=class java.lang.Integer]]]], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=class java.lang.Integer], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=MotorDuration [type=DEGREE, value=EmptyExpr [defVal=class java.lang.Integer]]]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_MotorDiffTurnForMissing.xml"));
    }

    @Test
    public void reverseTransformatinTurnFor() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorDiffTurnFor.xml");
    }

    @Test
    public void reverseTransformatinDiffTurn() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void reverseTransformatinTurnMissing() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorDiffTurnMissing.xml");
    }

    @Test
    public void reverseTransformatinForMissing() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_MotorDiffTurnForMissing.xml");
    }
}
