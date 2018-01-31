package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class DriveActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=69, y=196], DriveAction [FOREWARD, MotionParam [speed=NumConst [50], duration=null]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorDiffOn.xml"));
    }

    @Test
    public void reverseTransformatinMotorDiffOn() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorDiffOn.xml");
    }

    @Test
    public void getDirection() throws Exception {
        DriveAction<?> da = (DriveAction<?>) this.h.generateAST("/ast/actions/action_MotorDiffOnFor.xml");
        Assert.assertEquals(DriveDirection.FOREWARD, da.getDirection());
    }

    @Test
    public void reverseTransformatinMotorDiffOnFor() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorDiffOnFor.xml");
    }

    @Test
    public void reverseTransformatinMotorDiffOnFor1() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorDiffOnFor1.xml");
    }

    @Test
    public void getParam() throws Exception {
        DriveAction<?> da = (DriveAction<?>) this.h.generateAST("/ast/actions/action_MotorDiffOnFor.xml");
        Assert.assertEquals("MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]", da.getParam().toString());
    }

    @Test
    public void motorDiffOnFor() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=55], DriveAction [FOREWARD, MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorDiffOnFor.xml"));
    }

    @Test
    public void motorDiffOnForMissing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-7, y=54], DriveAction [FOREWARD, MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=MotorDuration [type=DISTANCE, value=EmptyExpr [defVal=NUMBER_INT]]]], DriveAction [FOREWARD, MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=EmptyExpr [defVal=NUMBER_INT]]]], DriveAction [FOREWARD, MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorDiffOnForMissing.xml"));
    }

    @Test
    public void reverseTransformatinMotorDiffOnForMissing() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorDiffOnForMissing.xml");
    }

    @Test
    public void motorDiffOnMissing() throws Exception {
        String a = "BlockAST [project=[[Location [x=-7, y=1], DriveAction [FOREWARD, MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=null]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorDiffOnMissing.xml"));
    }

    @Test
    public void reverseTransformatinMotorDiffOnMissing() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorDiffOnMissing.xml");
    }
}
