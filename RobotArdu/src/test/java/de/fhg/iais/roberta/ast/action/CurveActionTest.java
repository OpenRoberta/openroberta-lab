package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class CurveActionTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=113, y=288], CurveAction [FOREWARD, MotionParam [speed=NumConst [20], duration=null]MotionParam [speed=NumConst [50], duration=null]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorCurve.xml");
    }

    @Test
    public void reverseTransformatinMotorDiffOn() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorCurve.xml");
    }

    @Test
    public void motorDiffOnFor() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=138, y=238], CurveAction [FOREWARD, MotionParam [speed=NumConst [20], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorCurveFor.xml");
    }

    @Test
    public void getParam() throws Exception {
        CurveAction<?> da = (CurveAction<?>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/actions/action_MotorCurveFor.xml");
        Assert.assertEquals("MotionParam [speed=NumConst [20], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]", da.getParamLeft().toString());
    }

    @Test
    public void getDirection() throws Exception {
        CurveAction<?> da = (CurveAction<?>) UnitTestHelper.getAstOfFirstBlock(testFactory, "/ast/actions/action_MotorCurveFor.xml");
        Assert.assertEquals(DriveDirection.FOREWARD, da.getDirection());
    }

    @Test
    public void reverseTransformatinMotorDiffOnFor() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorCurveFor.xml");
    }

}
