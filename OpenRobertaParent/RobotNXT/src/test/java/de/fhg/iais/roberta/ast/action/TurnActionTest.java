package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class TurnActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=243], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorDiffTurnFor.xml"));
    }

    @Test
    public void getDirection() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_MotorDiffTurnFor.xml");
        TurnAction<Void> ta = (TurnAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(TurnDirection.RIGHT, ta.getDirection());
    }

    @Test
    public void getParam() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_MotorDiffTurnFor.xml");
        TurnAction<Void> ta = (TurnAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("MotionParam [speed=NumConst [50], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]", ta.getParam().toString());
    }

    @Test
    public void motorDiffTurn() throws Exception {
        String a = "BlockAST [project=[[Location [x=29, y=89], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=null]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorDiffTurn.xml"));
    }

    @Test
    public void motorDiffTurnMissing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=35, y=84], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=null]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorDiffTurnMissing.xml"));
    }

    @Test
    public void motorDiffTurnForMissing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=27, y=3], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=MotorDuration [type=DEGREE, value=EmptyExpr [defVal=NUMBER_INT]]]], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=MotorDuration [type=DEGREE, value=EmptyExpr [defVal=NUMBER_INT]]]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorDiffTurnForMissing.xml"));
    }

    @Test
    public void reverseTransformatinTurnFor() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorDiffTurnFor.xml");
    }

    @Test
    public void reverseTransformatinDiffTurn() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void reverseTransformatinTurnMissing() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorDiffTurnMissing.xml");
    }

    @Test
    public void reverseTransformatinForMissing() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorDiffTurnForMissing.xml");
    }
}
