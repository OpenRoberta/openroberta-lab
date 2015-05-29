package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.sensor.ev3.UltrasonicSensorMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class UltraSonicSensorModeTest {

    @Test
    public void testBuilder() {
        UltrasonicSensorMode mode = UltrasonicSensorMode.get("Distance");
        Assert.assertEquals(UltrasonicSensorMode.DISTANCE, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        UltrasonicSensorMode.get("ad");
    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        UltrasonicSensorMode.get("");
    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        UltrasonicSensorMode.get(null);
    }
}
