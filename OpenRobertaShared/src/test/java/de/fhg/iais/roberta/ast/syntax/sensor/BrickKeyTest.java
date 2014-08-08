package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class BrickKeyTest {

    @Test
    public void testBuilder() {
        BrickKey mode = BrickKey.get("DOWN");
        Assert.assertEquals(BrickKey.DOWN, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        BrickKey mode = BrickKey.get("ad");
    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        BrickKey mode = BrickKey.get("");
    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        BrickKey mode = BrickKey.get(null);
    }
}
