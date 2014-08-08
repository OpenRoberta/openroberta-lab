package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class ColorTest {

    @Test
    public void testBuilder() {
        Color mode = Color.get("Green");
        Assert.assertEquals(Color.GREEN, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        Color mode = Color.get("ad");

    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        Color mode = Color.get("");

    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        Color mode = Color.get(null);

    }
}
