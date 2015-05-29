package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.sensor.ev3.ColorSensorMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class ColorSensorModeTest {

    @Test
    public void testBuilder() {
        ColorSensorMode mode = ColorSensorMode.get("AMBIENTLIGHT");
        Assert.assertEquals(ColorSensorMode.AMBIENTLIGHT, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        ColorSensorMode.get("ad");
    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        ColorSensorMode.get("");
    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        ColorSensorMode.get(null);
    }
}
