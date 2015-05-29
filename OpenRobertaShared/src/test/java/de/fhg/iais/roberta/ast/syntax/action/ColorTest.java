package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.action.ev3.BrickLedColor;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class ColorTest {

    @Test
    public void testBuilder() {
        BrickLedColor mode = BrickLedColor.get("Green");
        Assert.assertEquals(BrickLedColor.GREEN, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        BrickLedColor.get("ad");
    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        BrickLedColor.get("");
    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        BrickLedColor.get(null);
    }
}
