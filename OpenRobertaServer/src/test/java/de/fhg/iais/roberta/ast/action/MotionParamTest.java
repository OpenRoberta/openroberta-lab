package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.MotionParam;
import de.fhg.iais.roberta.ast.syntax.action.MotorDuration;
import de.fhg.iais.roberta.ast.syntax.action.MotorMoveMode;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;

public class MotionParamTest {

    @Test
    public void make() throws Exception {
        NumConst numConst = NumConst.make("0");
        MotorDuration motorDuration = new MotorDuration(MotorMoveMode.DEGREE, numConst);
        MotionParam motionParam = new MotionParam.Builder().speed(numConst).duration(motorDuration).build();

        String a = "MotionParam [speed=NumConst [0], duration=MotorDuration [type=DEGREE, value=NumConst [0]]]";

        Assert.assertEquals(a, motionParam.toString());
    }

    @Test
    public void getSpeed() throws Exception {
        NumConst numConst = NumConst.make("0");
        MotorDuration motorDuration = new MotorDuration(MotorMoveMode.DEGREE, numConst);
        MotionParam motionParam = new MotionParam.Builder().speed(numConst).duration(motorDuration).build();

        Assert.assertEquals("NumConst [0]", motionParam.getSpeed().toString());
    }

    @Test
    public void getDuration() throws Exception {
        NumConst numConst = NumConst.make("0");
        MotorDuration motorDuration = new MotorDuration(MotorMoveMode.DEGREE, numConst);
        MotionParam motionParam = new MotionParam.Builder().speed(numConst).duration(motorDuration).build();

        Assert.assertEquals("MotorDuration [type=DEGREE, value=NumConst [0]]", motionParam.getDuration().toString());
    }

}
