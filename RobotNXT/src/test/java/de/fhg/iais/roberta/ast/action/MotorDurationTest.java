package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;

public class MotorDurationTest extends NxtAstTest {
    @Test
    public void clearDisplay() throws Exception {
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1"), null);
        MotorDuration<Void> motorDuration = new MotorDuration<>(MotorMoveMode.DEGREE, numConst);
        String a = "MotorDuration [type=DEGREE, value=NumConst [0]]";
        Assert.assertEquals(a, motorDuration.toString());
    }

    @Test
    public void getValue() throws Exception {
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1"), null);
        MotorDuration<Void> motorDuration = new MotorDuration<>(MotorMoveMode.DEGREE, numConst);
        Assert.assertEquals("NumConst [0]", motorDuration.getValue().toString());
    }

    @Test
    public void setValue() throws Exception {
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1"), null);
        MotorDuration<Void> motorDuration = new MotorDuration<>(MotorMoveMode.DEGREE, numConst);
        numConst = NumConst.make("1", BlocklyBlockProperties.make("1", "1"), null);
        motorDuration.setValue(numConst);
        Assert.assertEquals("NumConst [1]", motorDuration.getValue().toString());
    }

    @Test
    public void getType() throws Exception {
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1"), null);
        MotorDuration<Void> motorDuration = new MotorDuration<>(MotorMoveMode.DEGREE, numConst);
        Assert.assertEquals(MotorMoveMode.DEGREE, motorDuration.getType());
    }

    @Test
    public void setType() throws Exception {
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1"), null);
        MotorDuration<Void> motorDuration = new MotorDuration<>(MotorMoveMode.DEGREE, numConst);
        motorDuration.setType(MotorMoveMode.DISTANCE);
        Assert.assertEquals(MotorMoveMode.DISTANCE, motorDuration.getType());
    }

}
