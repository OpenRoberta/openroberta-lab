package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.sensor.ev3.GyroSensorMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class GyroSensorModeTest {

    @Test
    public void testBuilder() {
        GyroSensorMode mode = GyroSensorMode.get("Angle");
        Assert.assertEquals(GyroSensorMode.ANGLE, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        GyroSensorMode.get("ad");
    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        GyroSensorMode.get("");
    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        GyroSensorMode.get(null);
    }
}
