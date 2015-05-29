package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.action.ev3.MotorMoveMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class MotorMoveModeTest {

    @Test
    public void testBuilder() {
        MotorMoveMode mode = MotorMoveMode.get("DEGREE");
        Assert.assertEquals(MotorMoveMode.DEGREE, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        MotorMoveMode.get("ad");
    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        MotorMoveMode.get("");
    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        MotorMoveMode.get(null);
    }
}
