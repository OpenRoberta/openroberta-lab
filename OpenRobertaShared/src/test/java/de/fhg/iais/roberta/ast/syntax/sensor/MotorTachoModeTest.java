package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.sensor.ev3.MotorTachoMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class MotorTachoModeTest {

    @Test
    public void testBuilder() {
        MotorTachoMode mode = MotorTachoMode.get("Degree");
        Assert.assertEquals(MotorTachoMode.DEGREE, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        MotorTachoMode.get("ad");
    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        MotorTachoMode.get("");
    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        MotorTachoMode.get(null);
    }
}
