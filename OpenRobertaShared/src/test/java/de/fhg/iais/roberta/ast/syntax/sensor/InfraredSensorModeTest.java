package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class InfraredSensorModeTest {

    @Test
    public void testBuilder() {
        InfraredSensorMode mode = InfraredSensorMode.get("Distance");
        Assert.assertEquals(InfraredSensorMode.DISTANCE, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        InfraredSensorMode mode = InfraredSensorMode.get("ad");

    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        InfraredSensorMode mode = InfraredSensorMode.get("");

    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        InfraredSensorMode mode = InfraredSensorMode.get(null);

    }
}
