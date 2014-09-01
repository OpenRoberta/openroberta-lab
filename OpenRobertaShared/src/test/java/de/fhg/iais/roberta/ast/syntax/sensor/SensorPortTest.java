package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class SensorPortTest {

    @Test
    public void testBuilder() {
        SensorPort mode = SensorPort.get("S1");
        Assert.assertEquals(SensorPort.S1, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        SensorPort mode = SensorPort.get("ad");

    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        SensorPort mode = SensorPort.get("");

    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        SensorPort mode = SensorPort.get(null);

    }
}
