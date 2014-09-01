package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class MotorStopModeTest {

    @Test
    public void testBuilder() {
        MotorStopMode mode = MotorStopMode.get("Float");
        Assert.assertEquals(MotorStopMode.FLOAT, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        MotorStopMode mode = MotorStopMode.get("ad");

    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        MotorStopMode mode = MotorStopMode.get("");

    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        MotorStopMode mode = MotorStopMode.get(null);

    }
}
