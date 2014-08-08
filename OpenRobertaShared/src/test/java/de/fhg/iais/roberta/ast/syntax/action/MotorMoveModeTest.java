package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class MotorMoveModeTest {

    @Test
    public void testBuilder() {
        MotorMoveMode mode = MotorMoveMode.get("DEGREE");
        Assert.assertEquals(MotorMoveMode.DEGREE, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        MotorMoveMode mode = MotorMoveMode.get("ad");

    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        MotorMoveMode mode = MotorMoveMode.get("");

    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        MotorMoveMode mode = MotorMoveMode.get(null);

    }
}
