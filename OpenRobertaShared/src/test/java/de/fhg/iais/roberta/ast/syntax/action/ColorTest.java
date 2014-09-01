package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class ColorTest {

    @Test
    public void testBuilder() {
        BrickLedColor mode = BrickLedColor.get("Green");
        Assert.assertEquals(BrickLedColor.GREEN, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        BrickLedColor mode = BrickLedColor.get("ad");

    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        BrickLedColor mode = BrickLedColor.get("");

    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        BrickLedColor mode = BrickLedColor.get(null);

    }
}
